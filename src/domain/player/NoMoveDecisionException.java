package src.domain.player;

class NoMoveDecisionException extends Exception {
  public NoMoveDecisionException(String message) {
    super(message);
  }
}