package com.ty.winchat.listener;

import java.io.IOException;

public abstract class Listener extends Thread{
	/**�򿪼�����*/
	abstract void open() throws IOException;
	/**�رռ�����*/
	abstract void close() throws IOException;
}
