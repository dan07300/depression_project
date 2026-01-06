package io.depression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.depression.dto.WarningQueryDTO;
import io.depression.entity.WarningRecord;
import io.depression.vo.PageVO;
import io.depression.vo.WarningRecordVO;

/**
 * 预警服务接口
 */
public interface WarningService extends IService<WarningRecord> {

    /**
     * 分页查询预警列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageVO<WarningRecordVO> pageWarnings(WarningQueryDTO queryDTO);

    /**
     * 根据ID获取预警详情
     *
     * @param id 预警ID
     * @return 预警VO
     */
    WarningRecordVO getWarningById(Long id);

    /**
     * 处理预警
     *
     * @param id 预警ID
     * @param processorId 处理人ID
     * @param processRemark 处理备注
     * @return 预警VO
     */
    WarningRecordVO processWarning(Long id, Long processorId, String processRemark);
}
