package blokus.utils;

/**
 * Pair
 */
public class Pair<T1, T2> {
  private T1 t1;
  private T2 t2;

  public Pair(T1 t1, T2 t2) {
    this.t1 = t1;
    this.t2 = t2;
  }
  /**
   * @return the t1
   */
  public T1 getFirst() {
    return t1;
  }

  /**
   * @return the t2
   */
  public T2 getSecond() {
    return t2;
  }

  @Override
  public int hashCode() {
    return getFirst().hashCode() + getSecond().hashCode() * 23;
  }

  @Override
  public String toString() {
    return "(" + getFirst() + ", " + getSecond() + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Pair<?, ?>) {
      Pair<?, ?> p = (Pair<?, ?>) obj;
      return getFirst().equals(p.getFirst()) && getSecond().equals(p.getSecond());
    }
    return false;
  }
}