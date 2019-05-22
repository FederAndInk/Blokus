package blokus.model;

/**
 * PlayerType
 */
public enum PlayerType {
  USER("Utilisateur"), //
  AI("IA"), //
  RANDOM_PIECE("Piece aleatoire"), //
  RANDOM_PLAY("Coup aleatoire");

  private String name;

  PlayerType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
