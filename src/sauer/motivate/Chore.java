package sauer.motivate;

public class Chore {

  private final String description;
  private final float rewardAmount;
  private final String rewardUnit;
  private int  completed;

  public Chore(String description, float rewardAmount, String rewardUnit, int completed) {
    this.description = description;
    this.rewardAmount = rewardAmount;
    this.rewardUnit = rewardUnit;
    this.completed = completed;
  }

  @Override
  public String toString() {
    return getDescription() + "|" + getRewardAmount() + "|" + getRewardUnit() + "|" + completed;
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
