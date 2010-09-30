package tagger.autotagger;

import java.io.Serializable;
import java.util.Date;

/**
 * Class representing a version. Useful for auto-tagging algorithms, etc.
 * @author Or Shwartz
 */
public class Version implements Comparable<Version>, Serializable {

	private static final long serialVersionUID = 6430543322663676659L;
	
	private final int major;
	private final int minor;
	private final int micro;
	private final Date date;
	
	/**
	 * Initialize the version object.
	 * @param major part of the version.
	 * @param minor part of the version.
	 * @param micro part of the version.
	 * @param date of current version. 
	 */
	public Version(int major, int minor, int micro, Date date) {

		this.major = major;
		this.minor = minor;
		this.micro = micro;
		this.date = date;
	}
	
	/**
	 * Return the major part of the version.
	 * @return the major
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * Return the minor part of the version.
	 * @return the minor
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * Return the micro part of the version.
	 * @return Micro part of the version.
	 */
	public int getMicro() {
		return micro;
	}

	/**
	 * Return the date of this version. 
	 * @return Date of last version update.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Return string representation of version.
	 * @return String representation of version in format <I>major.minor.micro</I>.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	
		// Return String representing the version 
		return String.format("%d.%d.%d", major, minor, micro);
	}

	/**
	 * Compare this version with other version object.
	 * @return If this version is:<BR>
	 * <B>older</B> return <B>negative</B> integer<BR>
	 * the <B>same</B> return <B>zero</B><BR>
	 * <B>newer</B> return <B>positive</B> integer
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Version otherVersion) {

		int result;
		
		// Compare major part of the version
		result = major - otherVersion.major;
		if (result != 0) {
			
			// Versions have different major part - return comparison result
			return result;
		}

		// Compare minor part of the version
		result = minor - otherVersion.minor;
		if (result != 0) {

			// Versions have different minor part - return comparison result
			return result;
		}

		// Compare micro part of the version
		result = micro - otherVersion.micro;
		
		// Return micro part of version determines this comparison's
		// result. Return it.
		return result;
	}

	/**
	 * @return True if versions have same major, minor and
	 * micro version parts. False, otherwise.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		// Quick test, in case this and other reference same object
		if (obj == this) {
			
			// Objects are the same - so definitely equal
			return true;
		}

		// If given object is not a reference of Version
		if (!(obj instanceof Version)) {
			
			// Objects have different types - so definitely not equal
			return false;
		}

		// Return comparison result
		return (this.compareTo((Version)obj) == 0);
	}
}
