package com.ty.winchat.listener;

import java.io.IOException;

public abstract class Listener extends Thread{
	/**´ò¿ª¼àÌıÆ÷*/
	abstract void open() throws IOException;
	/**¹Ø±Õ¼àÌıÆ÷*/
	abstract void close() throws IOException;
}
