package com.tmall.buy.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExtDTO implements Externalizable {

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		in.readObject();
	}

}
