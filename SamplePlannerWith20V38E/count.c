#include <stdio.h>

int main()
{
	int i = 0;
	int temp = 0;
	int array[3];
	for(i = 0;i < 3; i++)
		array[i] = 0;
	while(i < 20)
	{
		scanf("%d", &temp);
		printf("%d\n",temp);
		array[temp]++;
		i++;
	}
	for(i = 0;i < 3; i++)
		printf("%d\t%d\n", i, array[i]);

}