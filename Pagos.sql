--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.4
-- Dumped by pg_dump version 9.2.4
-- Started on 2022-03-10 02:23:55

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 178 (class 3079 OID 11727)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1966 (class 0 OID 0)
-- Dependencies: 178
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 82101)
-- Name: clasificacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE clasificacion (
    id integer NOT NULL,
    descripcion character varying(255)
);


ALTER TABLE public.clasificacion OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 82099)
-- Name: clasificacion_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE clasificacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.clasificacion_id_seq OWNER TO postgres;

--
-- TOC entry 1967 (class 0 OID 0)
-- Dependencies: 170
-- Name: clasificacion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE clasificacion_id_seq OWNED BY clasificacion.id;


--
-- TOC entry 173 (class 1259 OID 82109)
-- Name: grupo_escala; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE grupo_escala (
    id integer NOT NULL,
    salario double precision NOT NULL,
    descripcion character varying(255)
);


ALTER TABLE public.grupo_escala OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 82107)
-- Name: grupo_escala_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE grupo_escala_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.grupo_escala_id_seq OWNER TO postgres;

--
-- TOC entry 1968 (class 0 OID 0)
-- Dependencies: 172
-- Name: grupo_escala_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE grupo_escala_id_seq OWNED BY grupo_escala.id;


--
-- TOC entry 175 (class 1259 OID 82117)
-- Name: pago; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pago (
    id integer NOT NULL,
    id_trabajador integer NOT NULL,
    fecha date NOT NULL,
    dias_trabajados double precision NOT NULL,
    pago_adicional double precision,
    evaluacion integer NOT NULL
);


ALTER TABLE public.pago OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 82115)
-- Name: pago_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pago_id_seq OWNER TO postgres;

--
-- TOC entry 1969 (class 0 OID 0)
-- Dependencies: 174
-- Name: pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE pago_id_seq OWNED BY pago.id;


--
-- TOC entry 169 (class 1259 OID 82093)
-- Name: trabajador; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE trabajador (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL,
    nombre2 character varying(50),
    apellido1 character varying(50) NOT NULL,
    apellido2 character varying(50) NOT NULL,
    ci character varying(11) NOT NULL,
    id_clasificacion integer NOT NULL,
    id_escala integer NOT NULL
);


ALTER TABLE public.trabajador OWNER TO postgres;

--
-- TOC entry 168 (class 1259 OID 82091)
-- Name: trabajador_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE trabajador_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trabajador_id_seq OWNER TO postgres;

--
-- TOC entry 1970 (class 0 OID 0)
-- Dependencies: 168
-- Name: trabajador_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE trabajador_id_seq OWNED BY trabajador.id;


--
-- TOC entry 177 (class 1259 OID 82125)
-- Name: utilidades; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE utilidades (
    id integer NOT NULL,
    id_trabajador integer NOT NULL,
    trimestre character varying(255) NOT NULL,
    pagoa double precision,
    pagob double precision,
    pagoc double precision,
    pagod double precision,
    pagoe double precision
);


ALTER TABLE public.utilidades OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 82123)
-- Name: utilidades_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE utilidades_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.utilidades_id_seq OWNER TO postgres;

--
-- TOC entry 1971 (class 0 OID 0)
-- Dependencies: 176
-- Name: utilidades_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE utilidades_id_seq OWNED BY utilidades.id;


--
-- TOC entry 1941 (class 2604 OID 82104)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY clasificacion ALTER COLUMN id SET DEFAULT nextval('clasificacion_id_seq'::regclass);


--
-- TOC entry 1942 (class 2604 OID 82112)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY grupo_escala ALTER COLUMN id SET DEFAULT nextval('grupo_escala_id_seq'::regclass);


--
-- TOC entry 1943 (class 2604 OID 82120)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago ALTER COLUMN id SET DEFAULT nextval('pago_id_seq'::regclass);


--
-- TOC entry 1940 (class 2604 OID 82096)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trabajador ALTER COLUMN id SET DEFAULT nextval('trabajador_id_seq'::regclass);


--
-- TOC entry 1944 (class 2604 OID 82128)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY utilidades ALTER COLUMN id SET DEFAULT nextval('utilidades_id_seq'::regclass);


--
-- TOC entry 1948 (class 2606 OID 82106)
-- Name: clasificacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY clasificacion
    ADD CONSTRAINT clasificacion_pkey PRIMARY KEY (id);


--
-- TOC entry 1950 (class 2606 OID 82114)
-- Name: grupo_escala_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupo_escala
    ADD CONSTRAINT grupo_escala_pkey PRIMARY KEY (id);


--
-- TOC entry 1952 (class 2606 OID 82122)
-- Name: pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT pago_pkey PRIMARY KEY (id);


--
-- TOC entry 1946 (class 2606 OID 82098)
-- Name: trabajador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trabajador
    ADD CONSTRAINT trabajador_pkey PRIMARY KEY (id);


--
-- TOC entry 1954 (class 2606 OID 82130)
-- Name: utilidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY utilidades
    ADD CONSTRAINT utilidades_pkey PRIMARY KEY (id);


--
-- TOC entry 1957 (class 2606 OID 82141)
-- Name: fkpago429022; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT fkpago429022 FOREIGN KEY (id_trabajador) REFERENCES trabajador(id);


--
-- TOC entry 1956 (class 2606 OID 82136)
-- Name: fktrabajador307494; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trabajador
    ADD CONSTRAINT fktrabajador307494 FOREIGN KEY (id_escala) REFERENCES grupo_escala(id);


--
-- TOC entry 1955 (class 2606 OID 82131)
-- Name: fktrabajador470547; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trabajador
    ADD CONSTRAINT fktrabajador470547 FOREIGN KEY (id_clasificacion) REFERENCES clasificacion(id);


--
-- TOC entry 1958 (class 2606 OID 82155)
-- Name: fkutilidades6779; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY utilidades
    ADD CONSTRAINT fkutilidades6779 FOREIGN KEY (id_trabajador) REFERENCES trabajador(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1965 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2022-03-10 02:23:56

--
-- PostgreSQL database dump complete
--

