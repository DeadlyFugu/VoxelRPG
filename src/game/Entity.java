package game;

public abstract class Entity {
	public int x,y,z;
	private World world;
	public Entity(int x, int y, int z, World world) {
		this.x = x;
		this.y = y;
		this.z = y;
		this.world = world;
	}
	public void setPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = y;
	}
	public abstract void update();
}
