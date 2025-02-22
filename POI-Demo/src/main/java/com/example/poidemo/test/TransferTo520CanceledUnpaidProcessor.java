package com.example.poidemo.test;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/13 21:33
 */
public class TransferTo520CanceledUnpaidProcessor extends AbstractOrderStateTransferProcessor{



    @Override
    protected void afterProcess() {
        super.afterProcess();
        System.out.println("执行子类TransferTo520CanceledUnpaidProcessor afterProcess");
    }

    public static void main(String[] args) {
        IOrderStateTransferProcessor transferTo520CanceledUnpaidProcessor = new TransferTo520CanceledUnpaidProcessor();
        transferTo520CanceledUnpaidProcessor.process();
    }


}
