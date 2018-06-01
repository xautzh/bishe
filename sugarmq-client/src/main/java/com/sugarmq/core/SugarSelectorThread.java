package com.sugarmq.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * 选择器线程
 * @author manzhizhen
 *
 */

public class SugarSelectorThread implements Runnable {
	private Selector selector;
	
	public SugarSelectorThread(Selector selector) {
		this.selector = selector;
	}

	@Override
	public void run() {
		int selectorNum;
		try {
			while(true) {
					selectorNum = selector.select();
				if(selectorNum == 0) {
					continue;
				}
				
				Set<SelectionKey> keySet = selector.selectedKeys();
				for(SelectionKey key : keySet) {
					if(key.isReadable()) {
						
					}
				}
				
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
