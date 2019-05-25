package blokus.model;

/**
 * PlayerType
 */
public enum PlayerType {
  USER("Utilisateur"), //
  AI("Minimax IA"), //
  MCAI("Monte Carlo Search Tree IA"), //
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
