package ru.tecon.leakDetection.cdi;

import ru.tecon.leakDetection.model.ObjProp;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Конвертер для выбора свойства объекта
 *
 * @author Aleksey Sergeev
 */
@FacesConverter("paramConverter")
public class ParamConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ValueExpression vex = context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(), "#{leakDetectionMB}", LeakDetectionMB.class);

        LeakDetectionMB defaultValues = (LeakDetectionMB) vex.getValue(context.getELContext());

        return defaultValues.getPropList().stream()
                .filter(propType -> propType.getPropId() == Integer.parseInt(value))
                .findFirst()
                .orElse(null);

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(((ObjProp) value).getPropId());
    }
}
