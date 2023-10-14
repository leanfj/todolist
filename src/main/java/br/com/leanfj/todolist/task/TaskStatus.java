package br.com.leanfj.todolist.task;

public enum TaskStatus {
    PENDING {
        @Override
        public String toString() {
            return "Pendente";
        }
    },
    STARTED {
        @Override
        public String toString() {
            return "Iniciada";
        }
    },
    FINISHED {
        @Override
        public String toString() {
            return "Finalizada";
        }
    },
    PAUSED {
        @Override
        public String toString() {
            return "Pausada";
        }
    },
}
