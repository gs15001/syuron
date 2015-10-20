package sample;

public class Heapsort {

	public int[] heap; // �f�[�^�̔z��
	public int num; // ���݂̗v�f��
	int target; // ���݂̒��ڃ��C��

	public static void main(String[] args) {
		int[] original = { 3, 7, 2, 6, 4, 5 };
		Heapsort hs = new Heapsort();

		hs.print(original, "original");
		hs.sort(original);
		hs.print(original, "original");
	}

	/*
	 * �}��
	 */
	public void insert(int a) {
		heap[num++] = a;
		//int i = num + 1, j = i / 2;//���ʂ�+1
		//int i = num, j = i / 2 + 1;//���ʂ�+1
		//int i = num, j = num / 2 + 1;//num/2
		int i = num, j = i / 2;//�����p�^�[��
		//while (heap[i - 1] < heap[j - 1]) {i>1�Ȃ�
		//while (i >= 1 && heap[i - 1] < heap[j - 1]) {i>=1
		//while (i < 1 && heap[i - 1] < heap[j - 1]) {�s�����~�X1
		//while (i > 1 && heap[i - 1] > heap[j - 1]) {�s�����~�X2
		//while (i > 1 && heap[i] < heap[j]) {-1�Ȃ�
		while (i > 1 && heap[i - 1] < heap[j - 1]) {//�����p�^�[��
			//����ւ�
			int t = heap[i - 1];
			//heap[j - 1] = heap[i - 1];//i��j�̓���ւ�
			heap[i - 1] = heap[j - 1];//�����p�^�[��
			heap[j - 1] = t;

			//i��j�̍X�V
			i = j;
			j = i / 2;
		}
		//print(heap, "heap");
	}

	/*
	 * �擪�̗v�f����菜���A�Ԃ�
	 */
	public int deletemin() {
		//�擪���o��
		int r = heap[0];
		
		//�Ō��擪�ɂ��ăq�[�v���č\�z
		heap[0] = heap[--num];
		int i = 1, j = i * 2;
		while (j <= num) {
			if (j + 1 <= num && heap[j - 1] > heap[j])
				j++;
			if (heap[i - 1] > heap[j - 1]) {
				int t = heap[i - 1];
				heap[i - 1] = heap[j - 1];
				heap[j - 1] = t;
			}
			i = j;
			j = i * 2;
		}
		//System.out.println("deletemin : " + r);
		return r;
	}

	/*
	 * �\�[�g
	 */
	public void sort(int[] a) {

		// �K�v�ȃq�[�v�p�z����m�ۂ��܂�
		heap = new int[a.length];
		num = 0;

		//print(heap,"heap");
		// �q�[�v�ɗv�f��ǉ����܂�
		for (target = 0; target < a.length; target++) {
			insert(a[target]);
		}
		//print(heap,"heap");

		// �q�[�v������o���Ȃ���z��Ɋi�[���܂��B
		for (target = 0; num > 0; target++) {
			a[target] = deletemin();
		}
		//print(a,"result");
	}

	public void print(int[] a, String t) {
		System.out.print(t + " : ");
		for (int e : a) {
			System.out.print(e + " ");
		}
		System.out.println();
	}

}
