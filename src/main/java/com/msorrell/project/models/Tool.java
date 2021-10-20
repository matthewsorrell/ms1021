package com.msorrell.project.models;

import com.msorrell.project.enums.ToolBrand;
import com.msorrell.project.enums.ToolType;

import java.util.Objects;

/**
 * Tool class.
 */
public class Tool {

    /**
     * The tool's type.
     */
    private final ToolType type;

    /**
     * The tool's brand.
     */
    private final ToolBrand brand;

    /**
     * The tool's code.
     */
    private final String code;

    /**
     * Constructor.
     * @param type ToolType
     * @param brand ToolBrand
     * @param code Tool code
     */
    public Tool(final ToolType type, final ToolBrand brand, final String code) {
        this.type = type;
        this.brand = brand;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Tool tool = (Tool) o;
        return type == tool.type && brand == tool.brand && code.equals(tool.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, brand, code);
    }

    public ToolType getType() {
        return type;
    }

    public ToolBrand getBrand() {
        return brand;
    }

    public String getCode() {
        return code;
    }

}
