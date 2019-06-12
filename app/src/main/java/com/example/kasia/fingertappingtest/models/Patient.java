package com.example.kasia.fingertappingtest.models;

import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by kfojc on 17.05.2018.
 */

public class Patient implements Serializable {

    private String name;
    private String age;
    private String sex;
    private String group;
    private String handedness;
    private String tremor;
    private String remarks;
    private static List<Patient> patients= new ArrayList<>();
    private static Patient activePatient;
    private static InputStream model;

    private int tapsR;
    private int tapsL;
    private int correctTapsR;
    private int correctTapsL;
    private int accuracyErrorsR;
    private int accuracyErrorsL;
    private int alternationErrorsR;
    private int alternationErrorsL;
    private double intervalR;
    private double intervalL;
    private double RLintervalR;
    private double LRintervalR;
    private double RLintervalL;
    private double LRintervalL;
    private String modelClass = "";

    private List<String> tapLineListR;
    private List<String> tapLineList;
    private List<String> accelLineListR;
    private List<String> accelLineList;


    public Patient(String name, String age, String sex, String group, String handedness, String tremor, String remarks) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.group = group;
        this.handedness = handedness;
        this.tremor = tremor;
        this.remarks = remarks;
    }

    public static void setModel(InputStream model) {
        Patient.model = model;
    }

    public static List<Patient> getPatients() {
        return patients;
    }

    public static void addPatient(Patient patient){
        patients.add(patient);
    }

    public String getDescription(){
        return age + "," + sex + "," + group + ","+ handedness + "," + tremor + "," + remarks;
    }

    public void setParams(boolean rightHand, List<String> tapList, List<Double> timeList){
        int alternationErrors=0, accuracyErrors=0, correctTaps=0, taps=0;

        taps = tapList.size();

        for (String l: tapList){
            if (l.equals("N"))
                accuracyErrors++;
        }

        for (int i=1; i<tapList.size();i++){
            if(tapList.get(i).equals("R")||tapList.get(i).equals("L")) {
                if (tapList.get(i).equals(tapList.get(i-1)))
                    alternationErrors++;
            }
        }

        correctTaps=taps-alternationErrors-accuracyErrors;

        double meanInterval = 20.0/taps;

        double RLintervalsSum = 0;
        double LRintervalsSum = 0;
        int RLnum = 0;
        int LRnum = 0;


        for (int i=0; i<tapList.size()-1; i++){
            if(tapList.get(i).equals("R")){
                if(tapList.get(i+1).equals("L")){
                    RLnum++;
                    RLintervalsSum += timeList.get(i+1) - timeList.get(i);
                }
            }
            else if(tapList.get(i).equals("L")){
                if(tapList.get(i+1).equals("R")){
                    LRnum++;
                    LRintervalsSum += timeList.get(i+1) - timeList.get(i);
                }
            }
        }

        double RLintervalsMean = RLintervalsSum/RLnum;
        double LRintervalsMean = LRintervalsSum/LRnum;


        if (rightHand){
            setTapsR(taps);
            setCorrectTapsR(correctTaps);
            setAccuracyErrorsR(accuracyErrors);
            setAlternationErrorsR(alternationErrors);
            setIntervalR(meanInterval);
            setRLintervalR(RLintervalsMean);
            setLRintervalR(LRintervalsMean);
        }
        else{
            setTapsL(taps);
            setCorrectTapsL(correctTaps);
            setAccuracyErrorsL(accuracyErrors);
            setAlternationErrorsL(alternationErrors);
            setIntervalL(meanInterval);
            setRLintervalL(RLintervalsMean);
            setLRintervalL(LRintervalsMean);
        }


    }


    @Override
    public String toString(){
        return  name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Patient){
            if(((Patient) obj).getName().equals(name)&& ((Patient) obj).getDescription().equals(getDescription()))
                return true;
            else
                return false;
        }
        return false;
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass() {

        try {
            int NUMBER_OF_ATTRIBUTES = 10;
            int NUMBER_OF_INSTANCES = 1;


            Attribute attribute1 = new Attribute("numOfTapsA");
            Attribute attribute2 = new Attribute("numOfTapsND");
            Attribute attribute3 = new Attribute("correctTapsND");
            Attribute attribute4 = new Attribute("intervalsA");
            Attribute attribute5 = new Attribute("intervalsND");
            Attribute attribute6 = new Attribute("RLintervalsA");
            Attribute attribute7 = new Attribute("RLintervalsND");
            Attribute attribute8 = new Attribute("LRintervalsA");
            Attribute attribute9 = new Attribute("LRintervalsND");

            List<String> classes = new ArrayList<String>() {
                {
                    add("under 60 y/o"); // cls nr 1
                    add("over 60 y/o"); // cls nr 2
                    add("PD"); // cls nr 3
                }
            };
            Attribute attributeClass = new Attribute("@@class@@", classes);


            FastVector fvWekaAttributes = new FastVector(NUMBER_OF_ATTRIBUTES);
            fvWekaAttributes.addElement(attribute1);
            fvWekaAttributes.addElement(attribute2);
            fvWekaAttributes.addElement(attribute3);
            fvWekaAttributes.addElement(attribute4);
            fvWekaAttributes.addElement(attribute5);
            fvWekaAttributes.addElement(attribute6);
            fvWekaAttributes.addElement(attribute7);
            fvWekaAttributes.addElement(attribute8);
            fvWekaAttributes.addElement(attribute9);
            fvWekaAttributes.addElement(attributeClass);


            String dominant = getDominant(tremor, handedness);

            Instances dataSet = new Instances("test",fvWekaAttributes,NUMBER_OF_INSTANCES);
            dataSet.setClassIndex(9);
            Instance example = new DenseInstance(NUMBER_OF_ATTRIBUTES);

            example.setValue(attribute1, (tapsR+tapsL)/2);
            example.setValue(attribute4, (intervalL+intervalR)/2);
            example.setValue(attribute6, (RLintervalL+RLintervalR)/2);
            example.setValue(attribute8, (LRintervalL+LRintervalR)/2);

            if (dominant.equals("R")) {

                example.setValue(attribute2, tapsL);
                example.setValue(attribute3, correctTapsL);
                example.setValue(attribute5, intervalL);
                example.setValue(attribute7, RLintervalL);
                example.setValue(attribute9, LRintervalL);
            }
            else{
                example.setValue(attribute2, tapsR);
                example.setValue(attribute3, correctTapsR);
                example.setValue(attribute5, intervalR);
                example.setValue(attribute7, RLintervalR);
                example.setValue(attribute9, LRintervalR);
            }


            dataSet.add(example);

            Classifier cls = (Classifier) weka.core.SerializationHelper.read(model);


            double result = cls.classifyInstance(dataSet.instance(0));

            String className = classes.get(new Double(result).intValue());
            modelClass = className ;


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private String getDominant(String tremor, String handedness){

        if (tremor.equals("R")) return "L";
        else if (tremor.equals("L")) return "R";
        else if (handedness.equals("R")) return "R";
        else return "L";
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getGroup() {
        return group;
    }

    public String getHandedness() {
        return handedness;
    }

    public String getTremor() {
        return tremor;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIntervalR() {
        return intervalR;
    }

    public void setIntervalR(double intervalR) {
        this.intervalR = intervalR;
    }

    public double getIntervalL() {
        return intervalL;
    }

    public void setIntervalL(double intervalL) {
        this.intervalL = intervalL;
    }

    public double getRLintervalR() {
        return RLintervalR;
    }

    public void setRLintervalR(double RLintervalR) {
        this.RLintervalR = RLintervalR;
    }

    public double getLRintervalR() {
        return LRintervalR;
    }

    public void setLRintervalR(double LRintervalR) {
        this.LRintervalR = LRintervalR;
    }

    public double getRLintervalL() {
        return RLintervalL;
    }

    public void setRLintervalL(double RLintervalL) {
        this.RLintervalL = RLintervalL;
    }

    public double getLRintervalL() {
        return LRintervalL;
    }

    public void setLRintervalL(double LRintervalL) {
        this.LRintervalL = LRintervalL;
    }

    public int getTapsR() {
        return tapsR;
    }

    public void setTapsR(int tapsR) {
        this.tapsR = tapsR;
    }

    public int getTapsL() {
        return tapsL;
    }

    public void setTapsL(int tapsL) {
        this.tapsL = tapsL;
    }

    public int getCorrectTapsR() {
        return correctTapsR;
    }

    public void setCorrectTapsR(int correctTapsR) {
        this.correctTapsR = correctTapsR;
    }

    public int getCorrectTapsL() {
        return correctTapsL;
    }

    public void setCorrectTapsL(int correctTapsL) {
        this.correctTapsL = correctTapsL;
    }

    public int getAccuracyErrorsR() {
        return accuracyErrorsR;
    }

    public void setAccuracyErrorsR(int accuracyErrorsR) {
        this.accuracyErrorsR = accuracyErrorsR;
    }

    public int getAccuracyErrorsL() {
        return accuracyErrorsL;
    }

    public void setAccuracyErrorsL(int accuracyErrorsL) {
        this.accuracyErrorsL = accuracyErrorsL;
    }

    public int getAlternationErrorsR() {
        return alternationErrorsR;
    }

    public void setAlternationErrorsR(int alternationErrorsR) {
        this.alternationErrorsR = alternationErrorsR;
    }

    public int getAlternationErrorsL() {
        return alternationErrorsL;
    }

    public void setAlternationErrorsL(int alternationErrorsL) {
        this.alternationErrorsL = alternationErrorsL;
    }

    public static Patient getActivePatient() {
        return activePatient;
    }

    public static void setActivePatient(Patient activePatient) {
        Patient.activePatient = activePatient;
    }

    public List<String> getTapLineListR() {
        return tapLineListR;
    }

    public void setTapLineListR(List<String> tapLineListR) {
        this.tapLineListR = tapLineListR;
    }

    public List<String> getTapLineList() {
        return tapLineList;
    }

    public void setTapLineList(List<String> tapLineList) {
        this.tapLineList = tapLineList;
    }

    public List<String> getAccelLineListR() {
        return accelLineListR;
    }

    public void setAccelLineListR(List<String> accelLineListR) {
        this.accelLineListR = accelLineListR;
    }

    public List<String> getAccelLineList() {
        return accelLineList;
    }

    public void setAccelLineList(List<String> accelLineList) {
        this.accelLineList = accelLineList;
    }

}
