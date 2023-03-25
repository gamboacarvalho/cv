package orm.uow;

public class Tracker<K>{
	private State state;
	public final Entity<K> entity;
	public Tracker(Entity<K> entity, State st){
		if(st != State.New && st != State.Clean)
			throw new IllegalArgumentException("Tracker just can be initialized with state Clean or New!");
		this.entity = entity;
		this.state = st;
  }

	public State getState() {
  	return state;
  }

  public void update() {
	  state = state.onUpdate();
  }
	public void commit(){
		state = state.onCommit();
	}
	public void delete(){
		state = state.onDelete();
	}
	
	public abstract static class State{
		private State(){}
		abstract State onCommit();
		abstract State onUpdate();
		abstract State onDelete();
		public static final State New= new State() {
			State onUpdate() {return New;}
			State onDelete() {return Deleted;}
			State onCommit() {return Clean;}
		};
		public static final State Clean = new State() {
			State onUpdate() {return Dirty;}
			State onDelete() {return ToDelete;}
			State onCommit() {return Clean;}
		};
		public static final State Dirty = new State() {
			State onUpdate() {return Dirty;}
			State onDelete() {return ToDelete;}
			State onCommit() {return Clean;}
		};
		public static final State ToDelete = new State() {
			State onUpdate() {throw new IllegalStateException("Tracker in ToDelete state cannot be updated!");}
			State onDelete() {return ToDelete;}
			State onCommit() {return Deleted;}
		};
		public static final State Deleted = new State() {
			State onUpdate() {throw new IllegalStateException();}
			State onDelete() {throw new IllegalStateException();}
			State onCommit() {throw new IllegalStateException();}
		};
	}

}
