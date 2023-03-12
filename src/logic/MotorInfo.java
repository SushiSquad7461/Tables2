package src.logic;

public class MotorInfo {
    String[] inputList; //13 items long
    public String subSystem;
    public String motorName;
    public String pdhPort;
    public String canID;

    public double speed;
    public boolean joystickScale;

    public boolean coast;
    public boolean motorInversed;
    public boolean solenoidOn;
    public boolean isIncluded;

    public double currLimit;
    public double encoderMin;
    public double encoderMax;

    public MotorInfo(String input) {
        if (input.split(" ").length>=10){
            inputList = input.split(" ");
            subSystem = inputList[0];
            motorName = inputList[1];
            canID = inputList[2];
            pdhPort = inputList[3];

            speed = Double.parseDouble(inputList[4]);
            joystickScale = inputList[5] == "false";
            coast = inputList[6] == "false"; //could accept int
            motorInversed = inputList[7] == "false";
            currLimit = Double.parseDouble(inputList[8]);
            encoderMin = Double.parseDouble(inputList[9]);
            encoderMax = Double.parseDouble(inputList[10]);
            solenoidOn = inputList[11] == "false";
            isIncluded = inputList[12] == "false";
        }
    }
}
