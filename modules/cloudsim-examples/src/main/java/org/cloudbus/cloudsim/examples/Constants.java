package org.cloudbus.cloudsim.examples;

public class Constants {
    public static final String BENCHMARK = "BENCHMARK";
    public static final String DVFS = "DVFS";
    public static final String THRESHOLD = "THRESHOLD";

    public static final int NUM_USER = 1;
    public static final boolean TRACE_EVENTS = true;
    public static final String DATA_CENTER_NAME = "My_Lovely_Data_Center";
    // DATA CENTER VARIABLES
    public static final int HOSTS_NUMBER = 100;
    public static final int HOSTS_MAX_POWER = 250; //in watts
    public static final double HOSTS_STATIC_POWER_PERCENTAGE = 0.7; // in percentage
    public static final int HOSTS_MIP = 1000;
    public static final int HOSTS_RAM = 20000; // 2GB
    public static final int HOSTS_STORAGE = 1000000; // 1TB
    public static final int HOSTS_BANDWITH = 10000000;
    public static final String HOSTS_ARCHITECTURE = "x86";
    public static final String HOSTS_OS = "Linux";
    public static final String HOSTS_VMM = "Xen";
    public static final double HOSTS_TIME_ZONE = 10.0;
    public static final double HOSTS_COST = 3.0;
    public static final double HOSTS_COST_PER_MEM = 0.05;
    public static final double HOSTS_COST_PER_STORAGE = 0.001;
    public static final double HOSTS_COST_PER_BANDWITH = 0.0;
    // BROKER VARIABLES
    public static final String BROKER_NAME = "Broker";
    // VM VARIABLES
    public static final int VM_NUMBER = 400;
    public static final int VM_MIPS = 250;
    public static final int VM_PE_NUMBER = 1;
    public static final int VM_RAM = 256;
    public static final int VM_BANDWITH = 2500;
    public static final int VM_SIZE = 10; // image size
    public static final String VM_VMM = "Xen";
    // CLOUDLET VARIABLES
    public static final int CLOUDLET_NUMBER = 400;
    public static final int CLOUDLET_LENGTH = 150000;
    public static final int CLOUDLET_PE = 1;
    public static final int CLOUDLET_FILE_SIZE = 0;
    public static final int CLOUDLET_OUTPUT_SIZE = 0;


}
