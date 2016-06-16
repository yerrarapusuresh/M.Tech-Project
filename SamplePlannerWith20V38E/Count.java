import java.util.Scanner;

public class Count
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int[] array = new int[3];

		while(in.hasNext())
		{
			int temp = in.nextInt();
			array[temp]++;
		}

		for(int i = 0; i < 3; i++)
			System.out.println(array[i]);
	}
}