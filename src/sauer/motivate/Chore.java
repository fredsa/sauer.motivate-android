package sauer.motivate;

public class Chore {

  private final String description;
  private final float rewardAmount;
  private final String rewardUnit;
  private boolean completed;

  public Chore(String description, float rewardAmount, String rewardUnit) {
    this.description = description;
    this.rewardAmount = rewardAmount;
    this.rewardUnit = rewardUnit;
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

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isCompleted() {
    return completed;
  }

}
