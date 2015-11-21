# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: consultation.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='consultation.proto',
  package='consultation',
  syntax='proto2',
  serialized_pb=b'\n\x12\x63onsultation.proto\x12\x0c\x63onsultation\"*\n\rRetortRequest\x12\x19\n\x11original_question\x18\x01 \x01(\t\"\x1d\n\x0bRetortReply\x12\x0e\n\x06retort\x18\x01 \x01(\t2U\n\x0f\x43\x61mpaignManager\x12\x42\n\x06Retort\x12\x1b.consultation.RetortRequest\x1a\x19.consultation.RetortReply\"\x00'
)
_sym_db.RegisterFileDescriptor(DESCRIPTOR)




_RETORTREQUEST = _descriptor.Descriptor(
  name='RetortRequest',
  full_name='consultation.RetortRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='original_question', full_name='consultation.RetortRequest.original_question', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=36,
  serialized_end=78,
)


_RETORTREPLY = _descriptor.Descriptor(
  name='RetortReply',
  full_name='consultation.RetortReply',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='retort', full_name='consultation.RetortReply.retort', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=b"".decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=80,
  serialized_end=109,
)

DESCRIPTOR.message_types_by_name['RetortRequest'] = _RETORTREQUEST
DESCRIPTOR.message_types_by_name['RetortReply'] = _RETORTREPLY

RetortRequest = _reflection.GeneratedProtocolMessageType('RetortRequest', (_message.Message,), dict(
  DESCRIPTOR = _RETORTREQUEST,
  __module__ = 'consultation_pb2'
  # @@protoc_insertion_point(class_scope:consultation.RetortRequest)
  ))
_sym_db.RegisterMessage(RetortRequest)

RetortReply = _reflection.GeneratedProtocolMessageType('RetortReply', (_message.Message,), dict(
  DESCRIPTOR = _RETORTREPLY,
  __module__ = 'consultation_pb2'
  # @@protoc_insertion_point(class_scope:consultation.RetortReply)
  ))
_sym_db.RegisterMessage(RetortReply)


import abc
from grpc.beta import implementations as beta_implementations
from grpc.early_adopter import implementations as early_adopter_implementations
from grpc.framework.alpha import utilities as alpha_utilities
from grpc.framework.common import cardinality
from grpc.framework.interfaces.face import utilities as face_utilities
class EarlyAdopterCampaignManagerServicer(object):
  """<fill me in later!>"""
  __metaclass__ = abc.ABCMeta
  @abc.abstractmethod
  def Retort(self, request, context):
    raise NotImplementedError()
class EarlyAdopterCampaignManagerServer(object):
  """<fill me in later!>"""
  __metaclass__ = abc.ABCMeta
  @abc.abstractmethod
  def start(self):
    raise NotImplementedError()
  @abc.abstractmethod
  def stop(self):
    raise NotImplementedError()
class EarlyAdopterCampaignManagerStub(object):
  """<fill me in later!>"""
  __metaclass__ = abc.ABCMeta
  @abc.abstractmethod
  def Retort(self, request):
    raise NotImplementedError()
  Retort.async = None
def early_adopter_create_CampaignManager_server(servicer, port, private_key=None, certificate_chain=None):
  import consultation_pb2
  import consultation_pb2
  method_service_descriptions = {
    "Retort": alpha_utilities.unary_unary_service_description(
      servicer.Retort,
      consultation_pb2.RetortRequest.FromString,
      consultation_pb2.RetortReply.SerializeToString,
    ),
  }
  return early_adopter_implementations.server("consultation.CampaignManager", method_service_descriptions, port, private_key=private_key, certificate_chain=certificate_chain)
def early_adopter_create_CampaignManager_stub(host, port, metadata_transformer=None, secure=False, root_certificates=None, private_key=None, certificate_chain=None, server_host_override=None):
  import consultation_pb2
  import consultation_pb2
  method_invocation_descriptions = {
    "Retort": alpha_utilities.unary_unary_invocation_description(
      consultation_pb2.RetortRequest.SerializeToString,
      consultation_pb2.RetortReply.FromString,
    ),
  }
  return early_adopter_implementations.stub("consultation.CampaignManager", method_invocation_descriptions, host, port, metadata_transformer=metadata_transformer, secure=secure, root_certificates=root_certificates, private_key=private_key, certificate_chain=certificate_chain, server_host_override=server_host_override)

class BetaCampaignManagerServicer(object):
  """<fill me in later!>"""
  __metaclass__ = abc.ABCMeta
  @abc.abstractmethod
  def Retort(self, request, context):
    raise NotImplementedError()

class BetaCampaignManagerStub(object):
  """The interface to which stubs will conform."""
  __metaclass__ = abc.ABCMeta
  @abc.abstractmethod
  def Retort(self, request, timeout):
    raise NotImplementedError()
  Retort.future = None

def beta_create_CampaignManager_server(servicer, pool=None, pool_size=None, default_timeout=None, maximum_timeout=None):
  import consultation_pb2
  import consultation_pb2
  request_deserializers = {
    ('consultation.CampaignManager', 'Retort'): consultation_pb2.RetortRequest.FromString,
  }
  response_serializers = {
    ('consultation.CampaignManager', 'Retort'): consultation_pb2.RetortReply.SerializeToString,
  }
  method_implementations = {
    ('consultation.CampaignManager', 'Retort'): face_utilities.unary_unary_inline(servicer.Retort),
  }
  server_options = beta_implementations.server_options(request_deserializers=request_deserializers, response_serializers=response_serializers, thread_pool=pool, thread_pool_size=pool_size, default_timeout=default_timeout, maximum_timeout=maximum_timeout)
  return beta_implementations.server(method_implementations, options=server_options)

def beta_create_CampaignManager_stub(channel, host=None, metadata_transformer=None, pool=None, pool_size=None):
  import consultation_pb2
  import consultation_pb2
  request_serializers = {
    ('consultation.CampaignManager', 'Retort'): consultation_pb2.RetortRequest.SerializeToString,
  }
  response_deserializers = {
    ('consultation.CampaignManager', 'Retort'): consultation_pb2.RetortReply.FromString,
  }
  cardinalities = {
    'Retort': cardinality.Cardinality.UNARY_UNARY,
  }
  stub_options = beta_implementations.stub_options(host=host, metadata_transformer=metadata_transformer, request_serializers=request_serializers, response_deserializers=response_deserializers, thread_pool=pool, thread_pool_size=pool_size)
  return beta_implementations.dynamic_stub(channel, 'consultation.CampaignManager', cardinalities, options=stub_options)
# @@protoc_insertion_point(module_scope)
