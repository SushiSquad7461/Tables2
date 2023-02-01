package src.logic;

public class MotorInfo {
    String[] inputList;
    public String subSystem;
    public String motorName;
    public double speed;
    public boolean coast;
    public boolean encoderDir;
    public double currLimit;
    public double encoderMin;
    public double encoderMax;

    public MotorInfo(String input) {
        inputList = input.split(" ");
        subSystem = inputList[0];
        motorName = inputList[1];
        speed = Double.parseDouble(inputList[2]);
        coast = inputList[3] == "false"; //could accept int
        encoderDir = inputList[4] == "false";
        currLimit = Double.parseDouble(inputList[5]);
        encoderMin = Double.parseDouble(inputList[6]);
        encoderMax = Double.parseDouble(inputList[7]);
    }

    public void motorName(double speed2, boolean coast2, boolean encoderDir2, int currLimit2,
            double encoderMin2, double encoderMax2) {
    }
}
