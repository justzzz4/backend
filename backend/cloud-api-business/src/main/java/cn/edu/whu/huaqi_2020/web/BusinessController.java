package cn.edu.whu.huaqi_2020.web;

import cc.eamon.open.auth.AuthGroup;
import cc.eamon.open.auth.Logical;
import cc.eamon.open.status.Status;
import cn.edu.whu.huaqi_2020.entities.business.Business;
import cn.edu.whu.huaqi_2020.service.impl.BusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Zhu yuhan
 * Date:2020/9/28 16:15
 **/
@Api(
        value = "圈子模块",
        tags = "圈子模块"
)
@RestController
@RequestMapping("api/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private RestTemplate restTemplate;

//    @AuthExpression("userId != nil")
    @ApiOperation(
            value = "查询圈子",
            notes = "查询圈子"
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @RequestMapping(
            value = "",
            method = RequestMethod.GET
    )
    public Map<String, Object> fetchBusiness(@RequestParam("id") String id){
        return Status.successBuilder()
                .addDataValue(businessService.selectByPrimaryKey(id))
                .map();
    }

    @AuthGroup("admin")
    @ApiOperation(
            value = "查询圈子筛选列表",
            notes = "查询圈子筛选列表"
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @RequestMapping(
            value = "filter",
            method = RequestMethod.POST
    )
    public Map<String, Object> fetchBusinessList(@RequestBody Business business){
        return Status.successBuilder()
                .addDataValue(businessService.selectByExample(business))
                .map();
    }

    @AuthGroup("admin")
    @ApiOperation(
            value = "发布实体",
            notes = "发布实体"
    )
    @RequestMapping(
            value = "",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @ResponseBody
    public Map<String, Object> post(@RequestBody Business postMapper) {
        return Status.successBuilder()
                .addDataValue(businessService.insert(postMapper))
                .map();
    }

    @AuthGroup("admin")
    @ApiOperation(
            value = "发布一组实体",
            notes = "发布一组实体"
    )
    @RequestMapping(
            value = "batch",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @ResponseBody
    public Map<String, Object> postBatch(@RequestBody ArrayList<Business> postMappers) {
        List<Map<String, Object>> insertMapList = new LinkedList<>();
        for (Business postMapper : postMappers) {
            insertMapList.add(businessService.insert(postMapper));
        }
        return Status.successBuilder()
                .addDataValue(insertMapList)
                .map();
    }

    @AuthGroup("admin")
    @ApiOperation(
            value = "更新实体",
            notes = "更新实体"
    )
    @RequestMapping(
            value = "",
            method = RequestMethod.PATCH
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @ResponseBody
    public Map<String, Object> patch(@RequestBody Business updateMapper) {
        return Status.successBuilder()
                .addDataValue(businessService.updateByPrimaryKeySelective(updateMapper))
                .map();
    }

    @AuthGroup(value = {"super","admin"},logical = Logical.OR)
    @ApiOperation(
            value = "删除实体",
            notes = "删除实体"
    )
    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @ResponseBody
    public Map<String, Object> delete(@RequestParam("entityKey") String entityKey) {
        return Status.successBuilder()
                .addDataValue(businessService.deleteByPrimaryKey(entityKey))
                .map();
    }

    @AuthGroup(value = {"super","admin"},logical = Logical.AND)
    @ApiOperation(
            value = "删除一组实体",
            notes = "删除一组实体"
    )
    @RequestMapping(
            value = "batch",
            method = RequestMethod.DELETE
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    @ResponseBody
    public Map<String, Object> deleteBatch(@RequestParam("entityKeys") ArrayList<String> entityKeys) {
        AtomicInteger count = new AtomicInteger();
        for (String entityKey : entityKeys) {
            count.addAndGet(businessService.deleteByPrimaryKey(entityKey));
        }
        return Status.successBuilder()
                .addDataValue(count.get())
                .map();
    }
}
