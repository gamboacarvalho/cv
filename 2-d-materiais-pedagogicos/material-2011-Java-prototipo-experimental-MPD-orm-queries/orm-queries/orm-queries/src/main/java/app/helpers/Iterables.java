package app.helpers;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Iterables {
	
	public static <T> int count(Iterable<T> it){
		int i=0;
		for(@SuppressWarnings("unused") T o : it)
			i++;
		return i;
	}
	
	public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate<T> predicate){
		return new Iterable<T>(){
			@Override
			public Iterator<T> iterator() {
				final Iterator<T> it = iterable.iterator();
				return new Iterator<T>() {
					T next=null;
					@Override
					public boolean hasNext() {
						if(next!=null)
							return true;
						while(it.hasNext()){
							if(predicate.apply(next=it.next())){
								return true;
							}
						}
						next = null;
						return false;
					}

					@Override
					public T next() {
						if(next==null)
							throw new NoSuchElementException();
						T n =next;
						next = null;
						return n; 
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
					
				};
			}
			
		};
	}
}
