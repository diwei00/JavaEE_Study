package com.example.poidemo.test;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/13 21:52
 */
public class TransferTo600CanceledUnpaidProcessor extends AbstractOrderStateTransferProcessor{


    @Override
    protected void afterProcess() {
        System.out.println("执行子类TransferTo520CanceledUnpaidProcessor afterProcess");
    }

    public static void main(String[] args) {
        IOrderStateTransferProcessor transferTo600CanceledUnpaidProcessor = new TransferTo600CanceledUnpaidProcessor();
        transferTo600CanceledUnpaidProcessor.process();
    }
}
