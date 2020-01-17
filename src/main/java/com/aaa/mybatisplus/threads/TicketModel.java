package com.aaa.mybatisplus.threads;

/**
 * description: 多线程抢票应用
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019-12-20
 */

/**
 * 多线程抢票应用
 */
public class TicketModel {


    public static void main(String[] args) {
        TicketModel ticketModel = new TicketModel();
        Ticket ticket = ticketModel.new Ticket(20);//设置余票数
        TicketConsumer ticketConsumer = ticketModel.new TicketConsumer("售票点1", ticket);
        TicketConsumer ticketConsumer2 = ticketModel.new TicketConsumer("售票点2", ticket);
        TicketConsumer ticketConsumer3 = ticketModel.new TicketConsumer("售票点3", ticket);
        new Thread(ticketConsumer).start();
        new Thread(ticketConsumer2).start();
        new Thread(ticketConsumer3).start();
    }

    /**
     *余票
     */
    class Ticket {
        int num;

        public Ticket(int num) {
            this.num = num;
        }
    }
    /**
     *售票端
     */
    class TicketConsumer implements Runnable {
        String name;
        Ticket ticket;

        public TicketConsumer(String name, Ticket ticket) {
            this.name = name;
            this.ticket = ticket;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (ticket) {
                    if (ticket.num > 0) {
                        System.out.println("名称" + name + "的线程ID是：" + Thread.currentThread().getId());
                        System.out.println(name + ":您成功购买到一张票，余票剩余" + (--ticket.num));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(name + ":余票不足，购买失败");
                        break;
                    }
                }
            }
        }
    }
}


