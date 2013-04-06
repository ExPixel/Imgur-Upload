package expixel.imgur.util;

public interface IProgress {
	public void start();
	public void progress( double progress );
	public void setMax( double max );
	public void setMin( double min );
	public void setValue( double value );
	public void finished();
}
