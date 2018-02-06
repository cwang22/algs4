import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        setEnergy();
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    private void setEnergy() {
        energy = new double[width()][height()];

        int[][] colors = new int[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                colors[x][y] = picture.getRGB(x, y);
            }
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) energy[x][y] = 1000;
                else {
                    int up = colors[x][y - 1];
                    int down = colors[x][y + 1];
                    int left = colors[x - 1][y];
                    int right = colors[x + 1][y];

                    double rx = Math.pow(((left >> 16) & 0xFF) - ((right >> 16) & 0xFF), 2);
                    double gx = Math.pow(((left >> 8) & 0xFF) - ((right >> 8) & 0xFF), 2);
                    double bx = Math.pow((left & 0xFF) - (right & 0xFF), 2);

                    double ry = Math.pow(((up >> 16) & 0xFF) - ((down >> 16) & 0xFF), 2);
                    double gy = Math.pow(((up >> 8) & 0xFF) - ((down >> 8) & 0xFF), 2);
                    double by = Math.pow((up & 0xFF) - (down & 0xFF), 2);

                    energy[x][y] = Math.sqrt(rx + gx + bx + ry + gy + by);
                }

            }
        }
    }

    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) throw new IllegalArgumentException();
        return energy[x][y];
    }

    public int[] findHorizontalSeam() {
        int[] seam = new int[width()];
        int length = height() * width();
        double[] distTo = new double[length];
        int[] nodeTo = new int[length];

        for (int i = 0; i < distTo.length; i++) {
            if (i < height()) distTo[i] = 0;
            else distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int x = 0; x < width() - 1; x++) {
            for (int y = 0; y < height(); y++) {
                if (y > 0 && distTo[index(x + 1, y - 1)] > distTo[index(x, y)] + energy(x, y)) {
                    distTo[index(x + 1, y - 1)] = distTo[index(x, y)] + energy(x, y);
                    nodeTo[index(x + 1, y - 1)] = index(x, y);
                }

                if (distTo[index(x + 1, y)] > distTo[index(x, y)] + energy(x, y)) {
                    distTo[index(x + 1, y)] = distTo[index(x, y)] + energy(x, y);
                    nodeTo[index(x + 1, y)] = index(x, y);
                }

                if (y < height() - 1 && distTo[index(x + 1, y + 1)] > distTo[index(x, y)] + energy(x, y)) {
                    distTo[index(x + 1, y + 1)] = distTo[index(x, y)] + energy(x, y);
                    nodeTo[index(x + 1, y + 1)] = index(x, y);
                }
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int index = -1;

        for (int y = 0; y < height(); y++) {
            if (distTo[index(width() - 1, y)] < min) {
                min = distTo[index(width() - 1, y)];
                index = index(width() - 1, y);
            }
        }

        for (int x = width() - 1; x >= 0; x--) {
            seam[x] = index - x * height();
            index = nodeTo[index];
        }

        return seam;
    }

    public int[] findVerticalSeam() {
        transpose();
        int[] seam = findHorizontalSeam();
        transpose();
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() == 1 || seam.length != width()) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Picture p = new Picture(width(), height() - 1);

        for (int x = 0; x < width(); x++) {
            int i = 0;
            for (int y = 0; y < height(); y++) {
                if (y != seam[x]) p.set(x, i++, picture.get(x, y));
            }
        }

        picture = p;
        setEnergy();
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() == 1 || seam.length != height()) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Picture p = new Picture(width() - 1, height());

        for (int y = 0; y < height(); y++) {
            int i = 0;
            for (int x = 0; x < width(); x++) {
                if (x != seam[y]) p.set(i++, y, picture.get(x, y));
            }
        }

        picture = p;
        setEnergy();
    }

    private int index(int x, int y) {
        return x * height() + y;
    }

    private void transpose() {
        Picture flippedPicture = new Picture(picture.height(), picture.width());
        double[][] flippedEnergy = new double[picture.height()][picture.width()];

        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                flippedPicture.set(j, i, picture.get(i, j));
                flippedEnergy[j][i] = energy[i][j];
            }
        }

        picture = flippedPicture;
        energy = flippedEnergy;
    }

    public static void main(String[] args) {

    }
}