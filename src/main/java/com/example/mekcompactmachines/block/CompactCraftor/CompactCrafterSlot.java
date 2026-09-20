package com.example.mekcompactmachines.block.CompactCraftor;

import com.example.mekcompactmachines.ModConstants;

import java.util.Arrays;
import java.util.List;

/**
 * CompactCrafterのスロット情報を保持する列挙型クラス
 */

public enum CompactCrafterSlot {

    OUTPUT(0, 114, 36, 1, true, null),
    CARDBOARD(1, 67, 36, 1, false, ModConstants.MOD_ID + ":" + ModConstants.SUBSPACE_CARDBOARD),
    INPUT_1(2, 27, 26, 1536, false, "mekanism:structural_glass"),
    INPUT_2(3, 27, 45, 200, false, "mekanism:induction_casing");

    private final int index;
    private final int maxStack;

    private final int xPos;
    private final int yPos;
    private final boolean isOutput;
    private final String itemName;

    CompactCrafterSlot(int index, int xPos, int yPos, int maxStack, boolean isOutput, String itemName) {
        this.index = index;
        this.maxStack = maxStack;
        this.itemName = itemName;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isOutput = isOutput;
    }

    public int index() {
        return index;
    }

    public int maxStack() {
        return maxStack;
    }

    public String itemName() {
        return itemName;
    }

    public int xPos() {
        return xPos;
    }

    public int yPos() {
        return yPos;
    }

    public boolean isOutput() {
        return isOutput;
    }

    /**
     * スロット番号からEnumを取得するメソッド
     *
     * @param index スロット番号
     * @return 該当するクラス1
     */
    public static CompactCrafterSlot fromIndex(int index) {
        for (CompactCrafterSlot slot : values()) {
            if (slot.index() == index) {
                return slot;
            }
        }
        return null;
    }

    /**
     * すべてのスロット情報をリスト形式で取得します。
     *
     * @return 全スロットが格納された List
     */
    public static List<CompactCrafterSlot> list() {
        return Arrays.asList(values());
    }
}