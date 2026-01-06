package io.depression.controller;

import io.depression.common.Result;
import io.depression.dto.PatientCreateDTO;
import io.depression.dto.PatientQueryDTO;
import io.depression.dto.PatientUpdateDTO;
import io.depression.service.PatientService;
import io.depression.vo.PageVO;
import io.depression.vo.PatientVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 患者表 前端控制器
 * </p>
 *
 * @author system
 * @since 2025-01-01
 */
@Api(tags = "患者管理")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "分页查询患者列表", notes = "根据条件分页查询患者列表")
    @GetMapping
    public Result<PageVO<PatientVO>> page(PatientQueryDTO queryDTO) {
        PageVO<PatientVO> page = patientService.pagePatients(queryDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "获取患者详情", notes = "根据ID获取患者详情")
    @GetMapping("/{id}")
    public Result<PatientVO> getById(@PathVariable Long id) {
        PatientVO patient = patientService.getPatientById(id);
        if (patient == null) {
            return Result.error("患者不存在");
        }
        return Result.success(patient);
    }

    @ApiOperation(value = "创建患者", notes = "创建新的患者记录")
    @PostMapping
    public Result<PatientVO> create(@Valid @RequestBody PatientCreateDTO dto) {
        try {
            PatientVO patient = patientService.createPatient(dto);
            return Result.success("创建成功", patient);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "更新患者", notes = "更新患者信息")
    @PutMapping("/{id}")
    public Result<PatientVO> update(@PathVariable Long id, @Valid @RequestBody PatientUpdateDTO dto) {
        try {
            dto.setId(id);
            PatientVO patient = patientService.updatePatient(dto);
            return Result.success("更新成功", patient);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除患者", notes = "逻辑删除患者")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            boolean success = patientService.deletePatient(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "患者迁移", notes = "将患者迁移到其他医院或医生")
    @PostMapping("/{id}/transfer")
    public Result<PatientVO> transfer(@PathVariable Long id, @RequestBody io.depression.dto.PatientTransferDTO dto) {
        try {
            dto.setPatientId(id);
            PatientVO patient = patientService.transferPatient(dto.getPatientId(), dto.getTargetCohortCode(), dto.getTargetDoctorId());
            return Result.success("迁移成功", patient);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}



