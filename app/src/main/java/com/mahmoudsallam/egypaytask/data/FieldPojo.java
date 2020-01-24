package com.mahmoudsallam.egypaytask.data;

public class FieldPojo implements Comparable<FieldPojo> {
    private String id;
    private String name;
    private String required;
    private String type;
    private String default_value;
    private String multiple;
    private String sort;

    public FieldPojo(String id, String name, String required, String type, String default_value, String multiple, String sort) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.type = type;
        this.default_value = default_value;
        this.multiple = multiple;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(FieldPojo other) {
        if ((Integer.valueOf(this.getSort()) > Integer.valueOf(other.getSort())))
            return 1;
        else if ((Integer.valueOf(this.getSort()) == Integer.valueOf(other.getSort())))
            return 0;
        else
            return -1;
    }
}
