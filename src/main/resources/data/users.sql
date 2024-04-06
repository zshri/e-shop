INSERT INTO public.users (oauth_id, id, username, email, oauth_provider, password) VALUES (null, 1, 'user', 'user@mail.com', null, '$2a$10$mDic0EFf/xtP2xEGXP.oq.XPPO89n6mFpgyEfMsM6szedGe5EH7qa');
INSERT INTO public.users (oauth_id, id, username, email, oauth_provider, password) VALUES (null, 2, 'admin', 'admin@mail.com', null, '$2a$10$UXEHjEASioTj96e1b8s6pOTtTSL5zMYH1d.AigLuX9XVgFH/Hk1p2');
INSERT INTO public.users (oauth_id, id, username, email, oauth_provider, password) VALUES (111631022, 3, 'zshri', null, 'GitHub', null);

ALTER SEQUENCE users_id_seq RESTART WITH 10