package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.depression.entity.CohortAuthorization;
import io.depression.entity.CohortCenter;
import io.depression.entity.Patient;
import io.depression.entity.WarningRecord;
import io.depression.mapper.CohortAuthorizationMapper;
import io.depression.mapper.CohortCenterMapper;
import io.depression.mapper.PatientMapper;
import io.depression.mapper.WarningRecordMapper;
import io.depression.service.DashboardService;
import io.depression.vo.DashboardVO;
import io.depression.vo.TrendDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页统计服务实现
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private CohortCenterMapper hospitalMapper;

    @Autowired
    private CohortAuthorizationMapper userMapper;

    @Autowired
    private WarningRecordMapper warningMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public DashboardVO getDashboardData(Long userId) {
        // 获取用户信息
        CohortAuthorization user = null;
        String cohortCode = null;
        Integer roleType = null;
        
        if (userId != null) {
            user = userMapper.selectById(userId);
            if (user != null) {
                cohortCode = user.getCohortCode();
                roleType = user.getRoleType();
            }
        }
        DashboardVO vo = new DashboardVO();

        try {
            // 统计医院数量（仅系统管理员显示）
            if (roleType == null || roleType == 1) {
                Long hospitalCount = hospitalMapper.selectCount(new LambdaQueryWrapper<CohortCenter>()
                        .eq(CohortCenter::getDeleted, 0)
                        .eq(CohortCenter::getStatus, 1));
                vo.setHospitalCount(hospitalCount != null ? hospitalCount : 0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vo.setHospitalCount(0L);
        }

        try {
            // 根据角色统计用户数
            if (roleType == null || roleType == 1) {
                // 系统管理员：统计所有用户
                Long userCount = userMapper.selectCount(new LambdaQueryWrapper<CohortAuthorization>()
                        .eq(CohortAuthorization::getDeleted, 0)
                        .eq(CohortAuthorization::getStatus, 1));
                vo.setUserCount(userCount != null ? userCount : 0L);
            } else if (roleType == 2 && cohortCode != null) {
                // 医院管理员：统计本院用户
                Long hospitalUserCount = userMapper.selectCount(new LambdaQueryWrapper<CohortAuthorization>()
                        .eq(CohortAuthorization::getDeleted, 0)
                        .eq(CohortAuthorization::getStatus, 1)
                        .eq(CohortAuthorization::getCohortCode, cohortCode));
                vo.setHospitalUserCount(hospitalUserCount != null ? hospitalUserCount : 0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vo.setUserCount(0L);
        }

        try {
            // 统计预警总数（根据角色和医院过滤）
            LambdaQueryWrapper<WarningRecord> warningWrapper = new LambdaQueryWrapper<WarningRecord>()
                    .eq(WarningRecord::getDeleted, 0);
            
            // 如果是医院管理员，需要根据患者所属医院过滤预警
            if (roleType != null && roleType == 2 && cohortCode != null) {
                // 获取本院所有患者的身份证号
                List<Patient> hospitalPatients = patientMapper.selectList(new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getDeleted, 0)
                        .eq(Patient::getCohortCode, cohortCode)
                        .select(Patient::getIdCard));
                if (hospitalPatients != null && !hospitalPatients.isEmpty()) {
                    List<String> idCards = hospitalPatients.stream()
                            .map(Patient::getIdCard)
                            .collect(Collectors.toList());
                    warningWrapper.in(WarningRecord::getIdCard, idCards);
                } else {
                    // 如果没有患者，预警数为0
                    warningWrapper.eq(WarningRecord::getId, -1); // 不可能匹配的条件
                }
            }
            
            Long warningCount = warningMapper.selectCount(warningWrapper);
            vo.setWarningCount(warningCount != null ? warningCount : 0L);
        } catch (Exception e) {
            e.printStackTrace();
            vo.setWarningCount(0L);
        }

        try {
            // 统计高风险预警数（根据角色和医院过滤）
            LambdaQueryWrapper<WarningRecord> highRiskWrapper = new LambdaQueryWrapper<WarningRecord>()
                    .eq(WarningRecord::getDeleted, 0)
                    .eq(WarningRecord::getWarningLevel, 3);
            
            // 如果是医院管理员，需要根据患者所属医院过滤预警
            if (roleType != null && roleType == 2 && cohortCode != null) {
                List<Patient> hospitalPatients = patientMapper.selectList(new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getDeleted, 0)
                        .eq(Patient::getCohortCode, cohortCode)
                        .select(Patient::getIdCard));
                if (hospitalPatients != null && !hospitalPatients.isEmpty()) {
                    List<String> idCards = hospitalPatients.stream()
                            .map(Patient::getIdCard)
                            .collect(Collectors.toList());
                    highRiskWrapper.in(WarningRecord::getIdCard, idCards);
                } else {
                    highRiskWrapper.eq(WarningRecord::getId, -1);
                }
            }
            
            Long highRiskWarningCount = warningMapper.selectCount(highRiskWrapper);
            vo.setHighRiskWarningCount(highRiskWarningCount != null ? highRiskWarningCount : 0L);
        } catch (Exception e) {
            e.printStackTrace();
            vo.setHighRiskWarningCount(0L);
        }

        try {
            // 统计患者风险等级分布（根据角色和医院过滤）
            Map<String, Long> riskDistribution = new HashMap<>();
            LambdaQueryWrapper<Patient> patientWrapper = new LambdaQueryWrapper<Patient>()
                    .eq(Patient::getDeleted, 0)
                    .eq(Patient::getStatus, 1);
            
            // 如果是医院管理员，只统计本院患者
            if (roleType != null && roleType == 2 && cohortCode != null) {
                patientWrapper.eq(Patient::getCohortCode, cohortCode);
            }
            
            List<Patient> patients = patientMapper.selectList(patientWrapper);
            
            if (patients != null) {
                // 根据患者状态和复发情况计算风险等级
                long highRisk = patients.stream()
                        .filter(p -> p.getIsRelapsed() != null && p.getIsRelapsed() == 1)
                        .count();
                long mediumRisk = patients.stream()
                        .filter(p -> p.getStatus() != null && p.getStatus() == 1 && 
                                   (p.getIsRelapsed() == null || p.getIsRelapsed() == 0))
                        .count();
                long lowRisk = patients.size() - highRisk - mediumRisk;
                long remission = 0; // 缓解期患者数，可根据实际业务逻辑计算

                riskDistribution.put("高风险", highRisk);
                riskDistribution.put("中风险", mediumRisk);
                riskDistribution.put("低风险", lowRisk);
                riskDistribution.put("缓解期", remission);
            } else {
                riskDistribution.put("高风险", 0L);
                riskDistribution.put("中风险", 0L);
                riskDistribution.put("低风险", 0L);
                riskDistribution.put("缓解期", 0L);
            }
            vo.setRiskLevelDistribution(riskDistribution);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Long> riskDistribution = new HashMap<>();
            riskDistribution.put("高风险", 0L);
            riskDistribution.put("中风险", 0L);
            riskDistribution.put("低风险", 0L);
            riskDistribution.put("缓解期", 0L);
            vo.setRiskLevelDistribution(riskDistribution);
        }

        try {
            // 统计近半年趋势数据
            List<TrendDataVO> trendData = getTrendData(userId, cohortCode, roleType);
            vo.setTrendData(trendData);
        } catch (Exception e) {
            e.printStackTrace();
            vo.setTrendData(new ArrayList<>());
        }

        return vo;
    }

    private List<TrendDataVO> getTrendData(Long userId, String cohortCode, Integer roleType) {
        List<TrendDataVO> trendData = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 获取近6个月的数据
        for (int i = 5; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            String month = monthStart.format(formatter);

            // 统计该月入组人数（根据角色和医院过滤）
            LambdaQueryWrapper<Patient> enrollmentWrapper = new LambdaQueryWrapper<Patient>()
                    .eq(Patient::getDeleted, 0)
                    .ge(Patient::getEnrollDate, monthStart)
                    .lt(Patient::getEnrollDate, monthStart.plusMonths(1));
            if (roleType != null && roleType == 2 && cohortCode != null) {
                enrollmentWrapper.eq(Patient::getCohortCode, cohortCode);
            }
            Long enrollmentCount = patientMapper.selectCount(enrollmentWrapper);

            // 统计该月高风险预警数（根据角色和医院过滤）
            LambdaQueryWrapper<WarningRecord> warningWrapper = new LambdaQueryWrapper<WarningRecord>()
                    .eq(WarningRecord::getDeleted, 0)
                    .eq(WarningRecord::getWarningLevel, 3)
                    .ge(WarningRecord::getCreateTime, monthStart.atStartOfDay())
                    .lt(WarningRecord::getCreateTime, monthStart.plusMonths(1).atStartOfDay());
            if (roleType != null && roleType == 2 && cohortCode != null) {
                List<Patient> hospitalPatients = patientMapper.selectList(new LambdaQueryWrapper<Patient>()
                        .eq(Patient::getDeleted, 0)
                        .eq(Patient::getCohortCode, cohortCode)
                        .select(Patient::getIdCard));
                if (hospitalPatients != null && !hospitalPatients.isEmpty()) {
                    List<String> idCards = hospitalPatients.stream()
                            .map(Patient::getIdCard)
                            .collect(Collectors.toList());
                    warningWrapper.in(WarningRecord::getIdCard, idCards);
                } else {
                    warningWrapper.eq(WarningRecord::getId, -1);
                }
            }
            Long highRiskWarningCount = warningMapper.selectCount(warningWrapper);

            TrendDataVO trend = new TrendDataVO();
            trend.setMonth(month);
            trend.setEnrollmentCount(enrollmentCount);
            trend.setHighRiskWarningCount(highRiskWarningCount);
            trendData.add(trend);
        }

        return trendData;
    }
}
