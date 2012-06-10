package sauer.motivate;


public class Chore {

private final String date;
  private final String description;
  private final float rewardAmount;
  private final String rewardUnit;
  private int  completed;

  public Chore(String date ,String description, float rewardAmount, String rewardUnit, int completed) {
    this.date = date;
    this.description = description;
    this.rewardAmount = rewardAmount;
    this.rewardUnit = rewardUnit;
    this.completed = completed;
  }

  @Override
  public String toString() {
    return getDate() + "|" + getDescription() + "|" + getRewardAmount() + "|" + getRewardUnit() + "|" + completed;
  }

  public String getDate() {
    return date;
  }

  public String getDescription() {
    return description;
  }

  public String getRewardUnit() {
    return rewardUnit;
  }

  public float getRewardAmount() {
    return rewardAmount;
  }

  public void setCompleted(int completed) {
    this.completed = completed;
  }

  public int getCompleted() {
    return completed;
  }

}
