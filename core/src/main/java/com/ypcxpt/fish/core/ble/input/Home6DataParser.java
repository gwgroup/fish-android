package com.ypcxpt.fish.core.ble.input;

import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.core.app.Constant;
import com.ypcxpt.fish.library.util.FormatUtils;
import com.ypcxpt.fish.library.util.HexString;
import com.ypcxpt.fish.library.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ypcxpt.fish.core.app.Constant.HAS_DATA;
import static com.ypcxpt.fish.core.app.Constant.NO_DATA;

/**
 * 按摩椅向手机发送的数据帧:
 * 包头	         地址	 数据0	 数据1	...	  数据16	   校验码
 * 0x03/0x06	 0x02	 0x__	 0x__	...	  0x__	   0x__
 * <p>
 * 共20段两位16进制数据（每段一个字节，共20个字节，BLE每次最多只能读20个字节，超过的部分会分批发送）
 * <p>
 * 每个数据N都包含至多b0~b7共8个状态，分别对应的为:
 * 0x01 0x02 0x04 0x08 0x010 0x020 0x40 0x80
 * 当我们拿到数据后，要“按位与”上如上8个值，如果结果不为0，说明包含如上状态。
 * 比如拿到数据0x83，"按位与"过后只有"0x01"、"0x02"、"0x80"3个结果不为0，则说明同时包含如上3个状态。
 */
public class Home6DataParser {
    /**
     * 一共多少段（一段等于2个字节/16进制位）
     */
    public static final int VALIDATE_SECTIONS = 20;

    /**
     * 一共多少个16进制位(字节)
     */
    public static final int VALIDATE_LENGTH = 2 * VALIDATE_SECTIONS;

    /**
     * @param text 接收到的原始数据.
     * @return 数据0 ~ 数据16.
     */
    public List<Integer> parse(String text) {
        if (StringUtils.isTrimEmpty(text) || text.length() != VALIDATE_LENGTH) return null;

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < VALIDATE_SECTIONS; i++) {
            if (i == 0 || i == 1 || i == VALIDATE_SECTIONS - 1) {
                continue;
            }
            String section = text.substring(i * 2, i * 2 + 2);
            list.add(HexString.hexStringToInt(section));
        }

        ListUtils.printHexString(list);
        return list;
    }

    /**
     * 解析段信息：该信息只有一个输入结果.
     *
     * @param data 待解析段信息.
     * @return 0~7 对应 b0~b7.
     */
    private int parseSingleResultSection(int data) {
        switch (data) {
            case 0x01:
                return 0;
            case 0x02:
                return 1;
            case 0x04:
                return 2;
            case 0x08:
                return 3;
            case 0x10:
                return 4;
            case 0x20:
                return 5;
            case 0x40:
                return 6;
            case 0x80:
                return 7;
            default:
                return NO_DATA;
        }
    }

    /**
     * 结果有可能返回{@link Constant#NO_DATA}.
     */
    private int getSingleResultData(List<Integer> parsedData, int section) {
        return parseSingleResultSection(parsedData.get(section));
    }

    /**
     * 解析段信息：该信息有多个输出结果.
     */
    private List<Boolean> parseMultiResultSection(int data) {
        List<Boolean> list = new ArrayList<>();
        list.add((data & 0x01) != 0);
        list.add((data & 0x02) != 0);
        list.add((data & 0x04) != 0);
        list.add((data & 0x08) != 0);
        list.add((data & 0x010) != 0);
        list.add((data & 0x020) != 0);
        list.add((data & 0x040) != 0);
        list.add((data & 0x080) != 0);
        return list;
    }

    /**
     * 列表永不为空，包含8位状态.
     */
    private List<Boolean> getMultiResultData(List<Integer> parsedData, int section) {
        return parseMultiResultSection(parsedData.get(section));
    }

    /**
     * 数据1：	系统运行状态指示
     * b0：	SystemRunFlag		开机指示
     * b1：	PauseRunFlag		暂停指示
     * b2：	ShoulerScanning		肩部扫描中
     * b3：	ShoulerAdjust		肩部位置手动调节
     * b4：	ShoulerScanFinished		肩部扫描完成
     * b5：	SpotRunFlag		定点
     * b6：	PartialRunFlag		区间
     * b7：	FullBackRunFlag		全身
     *
     * @return
     */
    public List<Boolean> getDeviceState(List<Integer> parsedData) {
        return getMultiResultData(parsedData, 1);
    }

    /**
     * 自动程序
     * <p>
     * 数据3：	手动按摩程序运行状态指示
     * b0：	Auto1RunFlag	活力恢复
     * b1：	Auto2RunFlag	肩颈护理
     * b2：	Auto3RunFlag    腰背舒缓
     * b3：	Auto4RunFlag	臀部塑型
     * b4：	Auto5RunFlag	牵引拉伸
     * b5：	Auto6RunFlag	智能呵护
     * b6：	Auto7RunFlag	指尖唤醒
     * b7：	Auto8RunFlag    商务休闲
     *
     * @return flag结果0~7分别对应如上b0~b7的状态.
     */
    public int getAuto3Mode(List<Integer> parsedData) {
        return getSingleResultData(parsedData, 3);
    }

    /**
     * 自动程序
     * <p>
     * 数据4：	手动按摩程序运行状态指示
     * b0：	Auto9RunFlag	助眠修养
     * b1：	Auto10RunFlag	缓解放松
     * b2：	Auto11RunFlag
     * b3：	Auto12RunFlag
     * b4：	Auto13RunFlag
     * b5：	Auto14RunFlag
     * b6：	Auto15RunFlag
     * b7：	Auto16RunFlag
     *
     * @return flag结果0~7分别对应如上b0~b7的状态.
     */
    public int getAuto4Mode(List<Integer> parsedData) {
        return getSingleResultData(parsedData, 4);
    }

    /**
     * 手法
     * <p>
     * 数据5：	手动按摩程序运行状态指示
     * b0：	KneadRunFlag		揉捏
     * b1：	TapRunFlag		    敲击
     * b2：	DualActionRunFlag   揉敲
     * b3：	ShiatsuRunFlag		指压
     * b4：	RollRunFlag		    推拿
     *
     * @return flag结果0~4分别对应如上b0~b4的状态.
     */
    public int getTechniqueMode(List<Integer> parsedData) {
        return getSingleResultData(parsedData, 5);
    }

    /**
     * 背滚位置(数据8+数据9的b0~b1) 和 气囊位置(数据9的b2~b7)
     * <p>
     * 数据8：	机芯位置指示
     * b0：	BackPoint1Flag		背部点位，底端
     * b1：	BackPoint2Flag
     * b2：	BackPoint3Flag
     * b3：	BackPoint4Flag
     * b4：	BackPoint5Flag
     * b5：	BackPoint6Flag
     * b6：	BackPoint7Flag
     * b7：	BackPoint8Flag
     * <p>
     * 数据9：	机芯位置指示
     * b0：	BackPoint9Flag
     * b1：	BackPoint10Flag		背部点位，顶端
     * <p>
     * b2：	FootGasRunFlag		脚部
     * b3：                     暂无
     * b4：	LegGasRunFlag		腿部
     * b5：	SeatGasRunFlag		座部
     * b6：	ArmsGasRunFlag		手臂
     * b7：	ShoulderGasRunFlag	肩部
     * <p>
     * 注：机芯“背部”与“座部”是并联的，同亮同暗.
     *
     * @return 列表中0~5分别代表：背滚位置和5个气囊的显示状态 .
     */
    public List<Integer> getBeiGun$AirScrPlace(List<Integer> parsedData) {
        int lowLevelData = getSingleResultData(parsedData, 8);
        List<Boolean> data = getMultiResultData(parsedData, 9);

        int highLevelData = NO_DATA;
        if (data.get(0)) {
            highLevelData = 0;
        } else if (data.get(1)) {
            highLevelData = 1;
        }
        int beiGunPos = NO_DATA;
        if (lowLevelData == NO_DATA) {
            if (highLevelData != NO_DATA) {
                beiGunPos = highLevelData + 8;
            }
        } else {
            beiGunPos = lowLevelData;
        }

        List<Integer> resultList = new ArrayList<>();
        resultList.add(beiGunPos);
        resultList.add(data.get(2) ? HAS_DATA : NO_DATA);
        resultList.add(data.get(4) ? HAS_DATA : NO_DATA);
        resultList.add(data.get(5) ? HAS_DATA : NO_DATA);
        resultList.add(data.get(6) ? HAS_DATA : NO_DATA);
        resultList.add(data.get(7) ? HAS_DATA : NO_DATA);

        data = null;
        return resultList;
    }

    /**
     * 数据14：	3D伸缩设置
     * b0：	gFlexLevel1		3D伸缩力度1
     * b1：	gFlexLevel2		3D伸缩力度2
     * b2：	gFlexLevel3		3D伸缩力度3
     * b3：	gFlexLevel4		3D伸缩力度4
     * b4：	gFlexLevel5		3D伸缩力度5
     *
     * @return flag结果0~4分别对应如上b0~b4的状态.
     */
    public int get3DStrength(List<Integer> parsedData) {
        return getSingleResultData(parsedData, 14);
    }

    /**
     * 数据12：	辅助功能运行标志
     * b1：	HeatRunFlag		        加热
     * b2：	SoleRollRunFlag		    脚底滚轮
     * b3：	CalvesRollRunFlag		腿肚滚轮
     * b4：	BluetoothRunFlag		音乐蓝牙
     *
     * @return 列表中1~4分别代表：b1~b4的状态.
     */
    public List<Boolean> getAssistFunctions(List<Integer> parsedData) {
        return getMultiResultData(parsedData, 12);
    }

    /**
     * 数据10：	按摩椅姿态指示
     * b6：	ZeroGRun1Flag		零重力1运行标志
     * b7：	ZeroGRun2Flag		零重力2运行标志
     * b6,b7两个标志位都为零代表零重力关，两个标志位都为1代表零重力3档
     *
     * @return flag结果0~2分别对应零重力1~3.
     */
    public int getZeroGravityLevel(List<Integer> parsedData) {
        List<Boolean> data = getMultiResultData(parsedData, 10);
        boolean gravity1 = data.get(6);
        boolean gravity2 = data.get(7);
        data = null;
        if (gravity1) {
            if (gravity2) {
                return 2;
            } else {
                return 0;
            }
        } else {
            if (gravity2) {
                return 1;
            } else {
                return NO_DATA;
            }
        }
    }

    /**
     * 数据15：	按摩时间(分钟)
     * 数据16：	按摩时间(秒)
     *
     * @return "00:00"
     */
    public String getTime(List<Integer> parsedData) {
        int minutes = parsedData.get(15);
        int seconds = parsedData.get(16);
        return FormatUtils.keep2Places(minutes) + ":" + FormatUtils.keep2Places(seconds);
    }

    /**
     * 按摩强度(b0~b4) 和  机芯幅度(b5~b7)
     * <p>
     * 数据6：	档位指示
     * b0：	Speed1Flag      速度1档
     * b1：	Speed2Flag		速度2档
     * b2：	Speed3Flag		速度3档
     * b3：	Speed4Flag		速度4档
     * b4：	Speed5Flag		速度5档
     * <p>
     * b5：	Width1Flag		宽度1档
     * b6：	Width2Flag		宽度2档
     * b7：	Width3Flag		宽度3档
     *
     * @return 列表中0~1分别代表：速度值和宽度值.
     */
    public List<Integer> getGearLevel1(List<Integer> parsedData) {
        List<Boolean> data = getMultiResultData(parsedData, 6);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (data.get(i)) {
                result.add(i);
            }
        }
        if (result.size() == 0) {
            result.add(NO_DATA);
        }

        for (int i = 5; i < 8; i++) {
            if (data.get(i)) {
                result.add(i);
            }
        }
        if (result.size() == 1) {
            result.add(NO_DATA);
        }

        data = null;
        return result;
    }

    /**
     * 气压强度
     * <p>
     * 数据7：	档位指示
     * b0：	Intensity1Flag		气压强度1档
     * b1：	Intensity2Flag		气压强度2档
     * b2：	Intensity3Flag   	气压强度3档
     * b3：	Intensity4Flag		气压强度4档
     * b4：	Intensity5Flag		气压强度5档
     */
    public int getGearLevel2(List<Integer> parsedData) {
        return getSingleResultData(parsedData, 7);
    }

    /**
     * 滚轮速度
     * <p>
     * 数据11：
     * b6：	CalvesRollSpeed1		腿部滚轮速度1		两个都为1代表速度三挡。
     * b7：	CalvesRollSpeed2		腿部滚轮速度2
     *
     * @return flag结果0~2分别对应速度1~3.
     */
    public int getGearLevel3(List<Integer> parsedData) {
        List<Boolean> data = getMultiResultData(parsedData, 11);
        boolean speed1 = data.get(6);
        boolean speed2 = data.get(7);
        data = null;
        if (speed1) {
            if (speed2) {
                return 2;
            } else {
                return 0;
            }
        } else {
            if (speed2) {
                return 1;
            } else {
                return NO_DATA;
            }
        }
    }

    public static Home6DataParser get() {
        return Singleton.sInstance;
    }

    private Home6DataParser() {
    }

    private static class Singleton {
        private static Home6DataParser sInstance = new Home6DataParser();
    }
}
