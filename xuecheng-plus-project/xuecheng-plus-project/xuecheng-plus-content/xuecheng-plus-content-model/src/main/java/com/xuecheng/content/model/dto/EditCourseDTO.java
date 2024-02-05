package com.xuecheng.content.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCourseDTO extends AddCourseDTO{
    @NotNull(message = "课程id不能为空")
    private Long id;
}
