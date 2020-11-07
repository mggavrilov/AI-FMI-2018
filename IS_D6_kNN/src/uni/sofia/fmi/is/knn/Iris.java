package uni.sofia.fmi.is.knn;

public class Iris {
	private double sepalLength;
	private double sepalWidth;
	private double petalLength;
	private double petalWidth;
	private String className;
	private double tempDistance;

	public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String className) {
		this.sepalLength = sepalLength;
		this.sepalWidth = sepalWidth;
		this.petalLength = petalLength;
		this.petalWidth = petalWidth;
		this.className = className;
	}

	public void euclideanDistance(Iris i) {
		double a = Math.pow(sepalLength - i.getSepalLength(), 2);
		double b = Math.pow(sepalWidth - i.getSepalWidth(), 2);
		double c = Math.pow(petalLength - i.getPetalLength(), 2);
		double d = Math.pow(petalWidth - i.getPetalWidth(), 2);

		this.tempDistance = Math.sqrt(a + b + c + d);
	}

	public double getSepalLength() {
		return sepalLength;
	}

	public void setSepalLength(double sepalLength) {
		this.sepalLength = sepalLength;
	}

	public double getSepalWidth() {
		return sepalWidth;
	}

	public void setSepalWidth(double sepalWidth) {
		this.sepalWidth = sepalWidth;
	}

	public double getPetalLength() {
		return petalLength;
	}

	public void setPetalLength(double petalLength) {
		this.petalLength = petalLength;
	}

	public double getPetalWidth() {
		return petalWidth;
	}

	public void setPetalWidth(double petalWidth) {
		this.petalWidth = petalWidth;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public double getTempDistance() {
		return tempDistance;
	}

	public void setTempDistance(double tempDistance) {
		this.tempDistance = tempDistance;
	}
}
