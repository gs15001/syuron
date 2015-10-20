package sample;

public class Heapsort2 {

	public int[] heap; // �f�[�^�̔z��
	public int num; // ���݂̗v�f��
	int target; // ���݂̒��ڃ��C��

	public static void main(String[] args) {
		int[] original = { 3, 7, 2, 6, 4, 5 };
		Heapsort2 hs = new Heapsort2();

		hs.print(original);
		hs.sort(original);
		hs.print(original);
	}

	/*
	 * �}��
	 */
	public void insert(int a) {
		heap[num++] = a;
		int i = num, j = i / 2;
		while (i > 1 && heap[i - 1] < heap[j - 1]) {
			int t = heap[i - 1];
			heap[i - 1] = heap[j - 1];
			heap[j - 1] = t;
			i = j;
			j = i / 2;
		}
	}

	/*
	 * �擪�̗v�f����菜���A�Ԃ�
	 */
	public int deletemin() {
		int r = heap[0];
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
		return r;
	}

	/*
	 * �\�[�g
	 */
	public void sort(int[] a) {

		// �K�v�ȃq�[�v�p�z����m�ۂ��܂�
		heap = new int[a.length];
		num = 0;

		// �q�[�v�ɗv�f��ǉ����܂�
		for (target = 0; target < a.length; target++) {
			insert(a[target]);
		}

		// �q�[�v������o���Ȃ���z��Ɋi�[���܂��B
		for (target = 0; num > 0; target++) {
			a[target] = deletemin();
		}
	}

	public void print(int[] a) {
		for (int e : a) {
			System.out.print(e + " ");
		}
		System.out.println();
	}

}
