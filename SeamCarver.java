import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture picture;
    private int height;
    private int width;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }

        else {
            this.picture = new Picture(picture);

            height = picture.height();
            width = picture.width();
        }
    }
 
    // current picture
    public Picture picture() {
        return new Picture(picture);
    }
 
    // width of current picture
    public int width() {
        return width;
    }
 
    // height of current picture
    public int height() {
        return height;
    }
 
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException();
        }

        else if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000.0;
        }

        else {
            int drx = Math.abs(picture.get(x - 1, y).getRed() - picture.get(x + 1, y).getRed());
            int dgx = Math.abs(picture.get(x - 1, y).getGreen() - picture.get(x + 1, y).getGreen());
            int dbx = Math.abs(picture.get(x - 1, y).getBlue() - picture.get(x + 1, y).getBlue());

            int dry = Math.abs(picture.get(x, y - 1).getRed() - picture.get(x, y + 1).getRed());
            int dgy = Math.abs(picture.get(x, y - 1).getGreen() - picture.get(x, y + 1).getGreen());
            int dby = Math.abs(picture.get(x, y - 1).getBlue() - picture.get(x, y + 1).getBlue());

            return Math.sqrt(Math.pow(drx, 2) + Math.pow(dgx, 2) + Math.pow(dbx, 2) + Math.pow(dry, 2) + Math.pow(dgy, 2) + Math.pow(dby, 2));
        }
    }
 
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture transpose = new Picture(height, width);

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                transpose.setRGB(col, row, picture.getRGB(row, col));
            }
        } 

        picture = transpose;
        int tempHeight = height;
        height = width;
        width = tempHeight;

        int[] result = findVerticalSeam();

        Picture original = new Picture(height, width);

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                original.setRGB(col, row, picture.getRGB(row, col));
            }
        } 

        picture = original;
        tempHeight = height;
        height = width;
        width = tempHeight;

        return result;
    }

    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = new double[height][width];
        double[][] distancesTo = new double[height][width];
        int[][] edgesTo = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energies[row][col] = energy(col, row);

                if (row == 0) {
                    distancesTo[row][col] = 0;
                }

                else {
                    distancesTo[row][col] = Double.POSITIVE_INFINITY;
                }

                edgesTo[row][col] = -1;
            }
        }

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                for (int i = col - 1; i <= col + 1; i++) {
                    if (i >= 0 && i < width) {
                        double weight = energies[row + 1][i];

                        if (distancesTo[row + 1][i] > distancesTo[row][col] + weight) {
                            distancesTo[row + 1][i] = distancesTo[row][col] + weight;
                            edgesTo[row + 1][i] = col;
                        }
                    }
                }
            }
        }

        int[] seam = new int[height];

        double minEnergy = Double.POSITIVE_INFINITY;
        int minCol = -1;

        for (int col = 0; col < picture.width(); col++) {
            if (minEnergy > distancesTo[height - 1][col]) {
                minCol = col;
                minEnergy = distancesTo[height - 1][col];
            }
        }

        for (int row = height - 1; row >= 0; row--) {
            seam[row] = minCol;
            minCol = edgesTo[row][minCol];
        }

        return seam;
    }
 
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width || height <= 1) {
            throw new IllegalArgumentException(); 
        }

        else {
            for (int i = 0; i < seam.length - 1; i++) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException(); 
                }
            }
    
            Picture transpose = new Picture(height, width);
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    transpose.setRGB(col, row, picture.getRGB(row, col));
                }
            } 
    
            picture = transpose;
            int tempHeight = height;
            height = width;
            width = tempHeight;
    
            removeVerticalSeam(seam);
    
            Picture original = new Picture(height, width);
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    original.setRGB(col, row, picture.getRGB(row, col));
                }
            }
    
            picture = original;
            tempHeight = height;
            height = width;
            width = tempHeight;
        }  
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height || width <= 1) {
            throw new IllegalArgumentException(); 
        }

        else {
            for (int i = 0; i < seam.length - 1; i++) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException(); 
                }
            }
    
            Picture temp = new Picture(width - 1, height);
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width - 1; col++) {
                    if (seam[row] < 0 || seam[row] > width -1) {
                        throw new IllegalArgumentException(); 
                    }

                    if (col < seam[row]) {
                        temp.setRGB(col, row, picture.getRGB(col, row));
                    }

                    else {
                        temp.setRGB(col, row, picture.getRGB(col + 1, row));
                    }
                }
            }

            picture = temp;
            width--;
        }
    }
 }