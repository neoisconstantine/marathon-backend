package com.ruoyi.web.controller.business;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Person;
import com.ruoyi.system.service.IPersonService;
import com.ruoyi.system.service.IRegistrationService;

/**
 * 参赛用户管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/person")
public class PersonController extends BaseController
{
    @Autowired
    private IPersonService personService;

    @Autowired
    private IRegistrationService registrationService;

    /**
     * 获取参赛用户列表
     */
    @PreAuthorize("@ss.hasPermi('business:person:list')")
    @GetMapping("/list")
    public TableDataInfo list(Person person)
    {
        startPage();
        List<Person> list = personService.selectPersonList(person);
        return getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:person:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(personService.selectPersonById(id));
    }

    /**
     * 新增参赛用户
     */
    @PreAuthorize("@ss.hasPermi('business:person:add')")
    @Log(title = "参赛用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Person person)
    {
        return toAjax(personService.insertPerson(person));
    }

    /**
     * 修改参赛用户
     */
    @PreAuthorize("@ss.hasPermi('business:person:edit')")
    @Log(title = "参赛用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Person person)
    {
        return toAjax(personService.updatePerson(person));
    }

    /**
     * 修改参赛用户状态
     */
    @PreAuthorize("@ss.hasPermi('business:person:edit')")
    @Log(title = "参赛用户", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/status")
    public AjaxResult changeStatus(@PathVariable Long id, Integer status)
    {
        return toAjax(personService.changePersonStatus(id, status));
    }

    /**
     * 绑定赛事（报名）
     */
    @PreAuthorize("@ss.hasPermi('business:person:edit')")
    @Log(title = "参赛用户", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/bind-event")
    public AjaxResult bindEvent(@PathVariable Long id, @RequestBody Map<String, Long> body)
    {
        Long eventId = body.get("eventId");
        if (StringUtils.isNull(eventId))
        {
            return error("赛事ID不能为空");
        }
        return toAjax(registrationService.bindPersonEvent(id, eventId));
    }

    /**
     * 解绑赛事（退赛处理）
     */
    @PreAuthorize("@ss.hasPermi('business:person:edit')")
    @Log(title = "参赛用户", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/unbind-event")
    public AjaxResult unbindEvent(@PathVariable Long id, @RequestBody Map<String, Long> body)
    {
        Long eventId = body.get("eventId");
        if (StringUtils.isNull(eventId))
        {
            return error("赛事ID不能为空");
        }
        return toAjax(registrationService.unbindPersonEvent(id, eventId));
    }

    /**
     * 删除参赛用户
     */
    @PreAuthorize("@ss.hasPermi('business:person:remove')")
    @Log(title = "参赛用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(personService.deletePersonByIds(ids));
    }

    /**
     * 导出参赛用户列表
     */
    @Log(title = "参赛用户", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:person:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Person person)
    {
        List<Person> list = personService.selectPersonList(person);
        ExcelUtil<Person> util = new ExcelUtil<Person>(Person.class);
        util.exportExcel(response, list, "参赛用户数据");
    }

    /**
     * 导入参赛用户数据
     */
    @Log(title = "参赛用户", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('business:person:import')")
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file, boolean updateSupport)
    {
        try
        {
            ExcelUtil<Person> util = new ExcelUtil<Person>(Person.class);
            List<Person> personList = util.importExcel(file.getInputStream());
            int count = 0;
            for (Person person : personList)
            {
                if (StringUtils.isBlank(person.getName()))
                {
                    continue;
                }
                personService.insertPerson(person);
                count++;
            }
            return success("导入成功，共导入 " + count + " 条数据");
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 下载参赛用户导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Person> util = new ExcelUtil<Person>(Person.class);
        util.importTemplateExcel(response, "参赛用户数据");
    }
}
