package sample;

public class Heapsort {

	public int[] heap;
	public int num;
	int target;

	public static void main(String[] args) {
		int[] original = { 3, 7, 2, 6, 4, 5 };
		Heapsort hs = new Heapsort();

		hs.print(original, "original");
		hs.sort(original);
		hs.print(original, "original");
	}

	public void insert(int a) {
		heap[num++] = a;
		// int i = num + 1, j = i / 2;// ���ʂ�+1 �z��O�Q��
		// int i = num, j = i / 2 + 1;// ���ʂ�+1 ���Ȃ�
		// int i = num, j = num / 2 + 1;// num/2 ���Ȃ�
		int i = num, j = i / 2;// �����p�^�[��

		// while (heap[i - 1] < heap[j - 1]) {// i>1�Ȃ� �z��O�Q��
		// while (i >= 1 && heap[i - 1] < heap[j - 1]) {// i>=1 �z��O�Q��
		// while (i < 1 && heap[i - 1] < heap[j - 1]) {// �s�����~�X1 ���ʊԈႢ
		// while (i > 1 && heap[i - 1] > heap[j - 1]) {// �s�����~�X2 ���ʊԈႢ
		// while (i > 1 && heap[i] < heap[j]) {// -1�Ȃ� �z��O�Q��
		while (i > 1 && heap[i - 1] < heap[j - 1]) {// �����p�^�[��
			// ����ւ�
			int t = heap[i - 1];
			// heap[j - 1] = heap[i - 1];// i��j�̓���ւ� ���ʊԈႢ
			heap[i - 1] = heap[j - 1];// �����p�^�[��
			heap[j - 1] = t;

			// i��j�̍X�V
			i = j;
			j = i / 2;
		}
		// print(heap, "heap");
	}

	public int deletemin() {
		// �擪���o��
		int r = heap[0];

		// �Ō��擪��
		// heap[0] = heap[num--] // �|�X�g�f�N�������g
		// heap[--num] = heap[0]; // �����t
		heap[0] = heap[--num];// �����p�^�[��

		int i = 0, j = i * 2 + 1;
		// int i = 1, j = i * 2 + 1; // ���ʂ�+1

		// �q�[�v���č\�z
		// while (j < num) { // =�Ȃ�
		// while (j >= num) { // �s�����t
		// while (i <= num) { // i�Ɣ�r
		while (j <= num) { // �����p�^�[��

			// j+1���ő�l(num)�����q(j��j+1)�ŏ���������I��
			// if (j + 1 <= num && heap[j] < heap[j + 1]) {// �s�����t
			// if (j + 1 >= num && heap[j] > heap[j + 1]) {// j+1��num�̕s�����t
			// if (heap[j] > heap[j + 1]) {// j+1 <= num�Ȃ�
			if (j + 1 <= num && heap[j] > heap[j + 1]) {// �����p�^�[��
				j++;
			}

			// �e�Ǝq�Ŏq�̂ق�����������Γ���ւ�
			// if (heap[i] < heap[j]) {// �s�����t
			if (heap[i] > heap[j]) {// �����p�^�[��
				int t = heap[i];
				heap[i] = heap[j];
				heap[j] = t;
			}
			i = j;
			j = i * 2;
		}
		// System.out.println("deletemin : " + r);
		return r;
	}

	public void sort(int[] a) {

		// �K�v�ȃq�[�v�p�z����m�ۂ��܂�
		heap = new int[a.length];
		num = 0;

		// print(heap,"heap");
		// �q�[�v�ɗv�f��ǉ����܂�
		for (target = 0; target < a.length; target++) {
			insert(a[target]);
		}
		// print(heap,"heap");

		// �q�[�v������o���Ȃ���z��Ɋi�[���܂��B
		for (target = 0; num > 0; target++) {
			a[target] = deletemin();
		}
		// print(a,"result");
	}

	public void print(int[] a, String t) {
		System.out.print(t + " : ");
		for (int e : a) {
			System.out.print(e + " ");
		}
		System.out.println();
	}

}
