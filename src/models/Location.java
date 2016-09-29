package models;

public class Location {
	private double lattitude;
	private double longitude;

	public Location(double lattitude, double longitude) {
		this.setLattitude(lattitude);
		this.setLongitude(longitude);
	}

	public synchronized double getLattitude() {
		return lattitude;
	}

	private synchronized void setLattitude(double lattitude) {
		if (lattitude >= 0 && lattitude <= 90) {
			this.lattitude = lattitude;
		}
	}

	public synchronized double getLongitude() {
		return longitude;
	}

	private synchronized void setLongitude(double longitude) {
		if (longitude >= 0 && longitude <= 180) {
			this.longitude = longitude;
		}
	}

}
