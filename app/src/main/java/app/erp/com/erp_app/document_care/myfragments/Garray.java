package app.erp.com.erp_app.document_care.myfragments;

public class Garray {

    public static String[] value = new String[50];
    {
        for (int i=0; i < 50; i++){
            value[i]="";
        }
    }

    public static String[] value2 = new String[100];
    {
        for (int i=0; i < 100; i++){
            value2[i]="0";
        }
    }

    public static int[][] PositionInfo = new int[50][2];
    {
        for (int i=0; i < 50; i++){
            for(int j =0;i<=2;j++) {//2차원 배열 사용 i = 사진 선택하는 칸의 position
                                    // j는 C와 PC의 position을 저장
                                    // 0 : C값 저장용
                                    // 1 : P값 저장용
                PositionInfo[i][j] = 0; //초기화
            }
        }
    }
}
