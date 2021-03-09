package sample;

import java.text.DecimalFormat;

public class TestFile {
    private String filename;
    private double spamProbability;
    private String actualClass;

    public TestFile(String filename, double spamProbability, String actualClass) 
    {
        this.filename = filename;
        this.spamProbability = spamProbability;
        this.actualClass = actualClass;
    }
    
    /** 
     * @return String
     */
    public String getFilename() 
    {
        return this.filename;
    }
    
    /** 
     * @return double
     */
    public double getSpamProbability() {
        return this.spamProbability;
    }
    
    /** 
     * @return String
     */
    public String getSpamProbRounded() 
    {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability);
    }
    
    /** 
     * @return String
     */
    public String getActualClass()
    {
        return this.actualClass;
    }
    
    /** 
     * @param value
     */
    public void setFilename(String value)
    { 
        this.filename = value; 
    }
    
    /** 
     * @param val
     */
    public void setSpamProbability(double val)
    {
        this.spamProbability = val; 
    }
    
    /** 
     * @param value
     */
    public void setActualClass(String value)
    {
        this.actualClass=value; 
    }
}