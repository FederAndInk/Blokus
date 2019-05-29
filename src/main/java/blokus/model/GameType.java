package blokus.model;

/**
 * GameType
 */
public enum GameType {
  BLOKUS("Blokus"), //
  DUO("Duo"); //

  String name;

  GameType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}