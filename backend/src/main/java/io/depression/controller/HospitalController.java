package io.depression.controller;

import io.depression.common.Result;
import io.depression.dto.HospitalCreateDTO;
import io.depression.dto.HospitalQueryDTO;
import io.depression.service.HospitalService;
import io.depression.vo.HospitalVO;
import io.depression.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 医院管理控制器
 */
@Api(tags = "医院管理")
@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "分页查询医院列表", notes = "根据条件分页查询医院列表")
    @GetMapping
    public Result<PageVO<HospitalVO>> page(HospitalQueryDTO queryDTO) {
        PageVO<HospitalVO> page = hospitalService.pageHospitals(queryDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "获取所有医院列表", notes = "获取所有启用的医院列表（不分页）")
    @GetMapping("/all")
    public Result<List<HospitalVO>> listAll() {
        List<HospitalVO> hospitals = hospitalService.listAllHospitals();
        return Result.success(hospitals);
    }

    @ApiOperation(value = "获取医院详情", notes = "根据编码获取医院详情")
    @GetMapping("/{cohortCode}")
    public Result<HospitalVO> getByCode(@PathVariable String cohortCode) {
        HospitalVO hospital = hospitalService.getHospitalByCode(cohortCode);
        if (hospital == null) {
            return Result.error("医院不存在");
        }
        return Result.success(hospital);
    }

    @ApiOperation(value = "创建医院", notes = "创建新医院")
    @PostMapping
    public Result<HospitalVO> create(@Valid @RequestBody HospitalCreateDTO dto) {
        try {
            HospitalVO hospital = hospitalService.createHospital(dto);
            return Result.success("创建成功", hospital);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "更新医院", notes = "更新医院信息")
    @PutMapping("/{cohortCode}")
    public Result<HospitalVO> update(@PathVariable String cohortCode, @Valid @RequestBody HospitalCreateDTO dto) {
        try {
            HospitalVO hospital = hospitalService.updateHospital(cohortCode, dto);
            return Result.success("更新成功", hospital);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除医院", notes = "逻辑删除医院")
    @DeleteMapping("/{cohortCode}")
    public Result<String> delete(@PathVariable String cohortCode) {
        try {
            boolean success = hospitalService.deleteHospital(cohortCode);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
