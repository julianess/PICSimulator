package simulator;

import simulator.controller.SingleLayoutController;

public class Stack {

	public static int [] stack = new int [8];
	public static short stack_pointer = 0;
	
	public static void writeStack()
	{
		stack[stack_pointer] = Programcounter.pc+1;
		stack_pointer ++;
		stack_pointer = (short) (stack_pointer & 7);
	}
	public static void getStack()
	{
		stack_pointer--;
		Programcounter.pc = stack[stack_pointer];
		if(stack_pointer < 0)
		{
			stack_pointer = 7;
		}
	}
	
	public static void clearStack(){
		stack_pointer = 0;
		for (int i = 0; i < stack.length; i++) {
			stack[i] = 0;
		}
	}
}
