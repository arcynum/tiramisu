package au.com.ifti.models;

import java.util.Set;

public class ReportModel extends Model {
  
  private String additionalInfo;
  private String creator;
  private String environment;
  private String fileName;
  private String link;
  private String platform;
  private String program;
  private String reportPath;
  private String reportType;
  private String schedule;
  private String storedProcessName;
  private Set<TagModel> tags;
  
  public ReportModel() {
    super();
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getProgram() {
    return program;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public String getReportPath() {
    return reportPath;
  }

  public void setReportPath(String reportPath) {
    this.reportPath = reportPath;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  public String getSchedule() {
    return schedule;
  }

  public void setSchedule(String schedule) {
    this.schedule = schedule;
  }

  public String getStoredProcessName() {
    return storedProcessName;
  }

  public void setStoredProcessName(String storedProcessName) {
    this.storedProcessName = storedProcessName;
  }

  public Set<TagModel> getTags() {
    return tags;
  }

  public void setTags(Set<TagModel> tags) {
    this.tags = tags;
  }

}
