package com.msorrell.project.enums;

/**
 * The brand of each tool.
 */
public enum ToolBrand {

    /**
     * Werner.
     */
    WERNER,

    /**
     * Stihl.
     */
    STIHL,

    /**
     * Rigid.
     */
    RIDGID,

    /**
     * Dewalt.
     */
    DEWALT;

    /**
     * Converts the enum to proper capitalization for output.
     * Ex. WERNER -> Werner
     * @return Tool brand with proper capitalization
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
