package src.logic;

public class MotorInfo {
    String[] inputList; //12 items long
    public String subSystem;
    public String motorName;
    public String pdhPort;
    public String canID;

    public double speed;
    public double joystickScale;

    public boolean coast;
    public boolean motorInversed;
    public boolean isIncluded;

    public double currLimit;
    public double encoderMin;
    public double encoderMax;

    public MotorInfo(String input) {
        inputList = input.split(" ");
        subSystem = inputList[0];
        motorName = inputList[1];
        pdhPort = inputList[2];
        canID = inputList[3];

        speed = Double.parseDouble(inputList[4]);
        joystickScale = Double.parseDouble(inputList[5]);
        coast = inputList[6] == "false"; //could accept int
        motorInversed = inputList[7] == "false";
        currLimit = Double.parseDouble(inputList[8]);
        encoderMin = Double.parseDouble(inputList[9]);
        encoderMax = Double.parseDouble(inputList[10]);
        isIncluded = inputList[11] == "false";
    }

    public void motorName(double speed2, boolean coast2, boolean encoderDir2, int currLimit2,
            double encoderMin2, double encoderMax2) {
    }
}
