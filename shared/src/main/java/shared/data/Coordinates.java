package shared.data;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x;
    private float y;

    /**
     * @param builder {@link Builder}
     */
    public Coordinates(Coordinates.Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
    }

    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates {x=" + x + ", y=" + y + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass())
            return false;
        
        Coordinates coords = (Coordinates) obj;
        return this.x == coords.x &&
               this.y == coords.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Class to build {@link Coordinates}.
     */
    public static class Builder {
        private int x;
        private float y;

        /**
         * Sets value of field {@code x}.
         * @param x coordinate
         * @return {@link Builder} instance
         */
        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        /**
         * Sets value of field {@code y}.
         * @param y coordinate
         * @return {@link Builder} instance
         */
        public Builder setY(float y) {
            this.y = y;
            return this;
        }

        /**
         * Returns new {@link Coordinates} instance with set fields.
         * @return {@link Coordinates} 
         */
        public Coordinates build() {
            return new Coordinates(this);
        }
    }
}
