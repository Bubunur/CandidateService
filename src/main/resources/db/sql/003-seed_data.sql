--liquibase formatted sql
--changeset bubunur:3

INSERT INTO candidates (
    id, name, email, phone, position, pos_label, city, telegram, total_exp,
    stack, education, verdict, summary, criteria, experience, questions, status, created_at, updated_at
) VALUES

(
    'asanov-bakyt', 'Асанов Бакыт Эркинович', 'asanov.bakyt@email.com', '+996 700 111222', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@asanov_dev', '~4 г.',
    'Java 17, Spring Boot 3, PostgreSQL, Kafka, Testcontainers, Gradle, Docker', 'КГТУ им. Раззакова, ИТ, 2021', 'FIT',
    'Backend-разработчик с 4 годами коммерческого опыта.',
    '[{"key": "java_spring", "result": "OK", "comment": "Java 17, Spring Boot 3 — 4 года"}]'::jsonb,
    '[{"period": "2023-03 — н.в.", "company": "ПКБ «Финтех KG»", "title": "Java Developer", "duration": "~2 г."}]'::jsonb,
    '["Kafka: как настраивали consumer group при нескольких репликах?"]'::jsonb,
    'NEW', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days'
),
(
    'usupov-marat', 'Усупов Марат Бакытбекович', 'usupov.marat@email.com', '+996 700 990011', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@marat_dev', '~2.5 г.',
    'Java 17, Spring Boot 3, PostgreSQL, Kafka', 'КРСУ, ИСТ, 2023', 'PARTIAL', 'Разработчик с 2.5 годами опыта.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'NEW', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days'
),
(
    'sydykov-bekzat', 'Сыдыков Бекзат Маратович', 'sydykov.bekzat@email.com', '+996 700 223344', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@bekzat_s', '~2 г.',
    'Java 17, Spring Boot 3, PostgreSQL', 'АУЦА, CS, 2024', 'PARTIAL', 'Выпускник с 2 годами опыта.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'NEW', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days'
),
(
    'toktosunov-nurlan', 'Токтосунов Нурлан Эрланович', 'toktosunov.nurlan@email.com', '+996 700 445566', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@nurlan_t', '~1.5 г.',
    'Java 17, Spring Boot, MongoDB', 'КНУ, ИТ, 2024', 'NO_FIT', 'Использует MongoDB, PostgreSQL нет.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'NEW', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'
),

(
    'mamytov-erlan', 'Мамытов Эрлан Сейткалиевич', 'mamytov.erlan@email.com', '+996 550 223344', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@erlan_java', '~3.5 г.',
    'Java 17, Spring Boot 3, PostgreSQL', 'КНУ, Информатика, 2022', 'FIT', 'Специализируется на REST API.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'IN_REVIEW', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '4 days'
),
(
    'zhaparov-timur', 'Жапаров Тимур Алмазбекович', 'zhaparov.timur@email.com', '+996 700 667788', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@timur_j', '~3 г.',
    'Java 11, Spring Boot 2, MySQL', 'КНУ, Математика, 2022', 'PARTIAL', 'Хорошо знает Spring, но MySQL.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'IN_REVIEW', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '3 days'
),
(
    'musaev-azamat', 'Мусаев Азамат Нурланович', 'musaev.azamat@email.com', '+996 550 112233', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@azamat_m', '~3 г.',
    'Java 8, Spring 5, PostgreSQL', 'КНУ, Математика, 2022', 'PARTIAL', 'Устаревший стек Java 8.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'IN_REVIEW', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '1 day'
),

(
    'omorov-ruslan', 'Оморов Руслан Кубанычбекович', 'omorov.ruslan@email.com', '+996 700 334455', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@ruslan_backend', '~5 г.',
    'Java 17, Spring Boot, Kafka, Kubernetes', 'АУЦА, CS, 2020', 'FIT', 'Опытный разработчик в микросервисах.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'INVITED', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '2 days'
),
(
    'kubatov-daniyar', 'Кубатов Данияр Айбекович', 'kubatov.daniyar@email.com', '+996 550 778899', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@daniyar_k', '~2 г.',
    'Java 17, Spring Boot 3, PostgreSQL', 'АУЦА, CS, 2023', 'PARTIAL', 'Молодой разработчик с 2 годами опыта.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'INVITED', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '1 day'
),

(
    'tilekov-aibek', 'Тилеков Айбек Мирланович', 'tilekov.aibek@email.com', '+996 502 445566', 'java-middle', 'Java — ведущий программист', 'Ош', '@aibek_dev', '~3 г.',
    'Java 17, Spring Boot 3, OpenAPI', 'ОшТУ, ИТ, 2022', 'FIT', 'Хорошо понимает контрактное API.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'APPROVED', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP
),

(
    'kadyrov-nurbol', 'Кадыров Нурбол Бектурович', 'kadyrov.nurbol@email.com', '+996 777 556677', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@nurbol_java', '~4 г.',
    'Java 17, Spring Boot 3', 'КГТУ, АИТ, 2021', 'FIT', 'Уверенный в REST.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'REJECTED', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '3 days'
),
(
    'borbiev-almaz', 'Борбиев Алмаз Токтосунович', 'borbiev.almaz@email.com', '+996 702 889900', 'java-middle', 'Java — ведущий программист', 'Жалал-Абад', '@almaz_b', '~3 г.',
    'Java 17, Spring Boot, PostgreSQL', 'ЖАУ, ИТ, 2022', 'PARTIAL', 'Опыта с микросервисами нет.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'REJECTED', CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP - INTERVAL '4 days'
),
(
    'kydyraliev-nurzat', 'Кыдыралиев Нурзат Бакытович', 'kydyraliev.nurzat@email.com', '+996 550 334455', 'java-middle', 'Java — ведущий программист', 'Бишкек', '@nurzat_k', '~1 г.',
    'Java 11, Spring MVC, MySQL', 'КГТУ, ИТ, 2025', 'NO_FIT', 'Стек устаревший.',
    '[]'::jsonb, '[]'::jsonb, '[]'::jsonb,
    'REJECTED', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '1 day'
);

INSERT INTO candidate_status_history (id, candidate_id, from_status, to_status, comment, changed_at) VALUES
(gen_random_uuid()::varchar, 'mamytov-erlan', 'NEW', 'IN_REVIEW', 'Скрининг пройден', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(gen_random_uuid()::varchar, 'zhaparov-timur', 'NEW', 'IN_REVIEW', 'Скрининг пройден', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(gen_random_uuid()::varchar, 'musaev-azamat', 'NEW', 'IN_REVIEW', 'Скрининг пройден', CURRENT_TIMESTAMP - INTERVAL '1 day'),

(gen_random_uuid()::varchar, 'omorov-ruslan', 'NEW', 'IN_REVIEW', 'Скрининг', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(gen_random_uuid()::varchar, 'omorov-ruslan', 'IN_REVIEW', 'INVITED', 'Назначено тех. интервью', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(gen_random_uuid()::varchar, 'kubatov-daniyar', 'NEW', 'IN_REVIEW', 'Скрининг', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(gen_random_uuid()::varchar, 'kubatov-daniyar', 'IN_REVIEW', 'INVITED', 'Назначено интервью', CURRENT_TIMESTAMP - INTERVAL '1 day'),

(gen_random_uuid()::varchar, 'tilekov-aibek', 'NEW', 'IN_REVIEW', 'Скрининг', CURRENT_TIMESTAMP - INTERVAL '8 days'),
(gen_random_uuid()::varchar, 'tilekov-aibek', 'IN_REVIEW', 'INVITED', 'Интервью', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(gen_random_uuid()::varchar, 'tilekov-aibek', 'INVITED', 'APPROVED', 'Оффер принят', CURRENT_TIMESTAMP),

(gen_random_uuid()::varchar, 'kadyrov-nurbol', 'NEW', 'IN_REVIEW', 'Скрининг', CURRENT_TIMESTAMP - INTERVAL '6 days'),
(gen_random_uuid()::varchar, 'kadyrov-nurbol', 'IN_REVIEW', 'REJECTED', 'Не подходит по ЗП', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(gen_random_uuid()::varchar, 'kydyraliev-nurzat', 'NEW', 'IN_REVIEW', 'Скрининг', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(gen_random_uuid()::varchar, 'kydyraliev-nurzat', 'IN_REVIEW', 'REJECTED', 'Слабый технический бэкграунд', CURRENT_TIMESTAMP - INTERVAL '1 day'),

(gen_random_uuid()::varchar, 'borbiev-almaz', 'NEW', 'IN_REVIEW', 'Скрининг', CURRENT_TIMESTAMP - INTERVAL '7 days'),
(gen_random_uuid()::varchar, 'borbiev-almaz', 'IN_REVIEW', 'INVITED', 'Интервью', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(gen_random_uuid()::varchar, 'borbiev-almaz', 'INVITED', 'REJECTED', 'Не прошел тех. собеседование', CURRENT_TIMESTAMP - INTERVAL '4 days');